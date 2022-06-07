package wayoftime.bloodmagic.common.item.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import wayoftime.bloodmagic.entity.projectile.EntityPotionFlask;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class ItemAlchemyFlaskThrowable extends ItemAlchemyFlask
{
	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.NONE;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (getRemainingUses(stack) <= 0)
		{
			return ActionResult.resultPass(stack);
		}

		List<EffectHolder> holderList = getEffectHoldersOfFlask(stack);
		if (holderList.size() <= 0)
		{
			return ActionResult.resultPass(stack);
		}

		world.playSound((PlayerEntity) null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote)
		{
			EntityPotionFlask potionentity = new EntityPotionFlask(world, player);
			potionentity.setItem(stack);
			potionentity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
			prepPotionFlask(stack, player, potionentity);
			world.addEntity(potionentity);
		}

		player.addStat(Stats.ITEM_USED.get(this));
		if (!player.abilities.isCreativeMode)
		{
			stack.setDamage(stack.getDamage() + 1);
		}

		return ActionResult.func_233538_a_(stack, world.isRemote());
	}

	public void prepPotionFlask(ItemStack stack, PlayerEntity player, EntityPotionFlask potionEntity)
	{

	}

	public double getDurationModifier(ItemStack stack)
	{
		return 1;
	}

	@Override
	public void resyncEffectInstances(ItemStack stack)
	{
		double durationModifier = getDurationModifier(stack);
		List<EffectHolder> holderList = getEffectHoldersOfFlask(stack);
		List<EffectInstance> effectList = new ArrayList<EffectInstance>();

		for (EffectHolder holder : holderList)
		{
			effectList.add(holder.getEffectInstance(durationModifier, false, true));
		}

		setEffectsOfFlask(stack, effectList);
		Collection<EffectInstance> instanceList = PotionUtils.getEffectsFromStack(stack);

		int color = instanceList.isEmpty() ? PotionUtils.getPotionColor(Potions.WATER)
				: PotionUtils.getPotionColorFromEffectList(instanceList);
		stack.getTag().putInt("CustomPotionColor", color);
	}
}
