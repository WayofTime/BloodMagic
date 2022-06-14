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
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.NONE;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (getRemainingUses(stack) <= 0)
		{
			return ActionResult.pass(stack);
		}

		List<EffectHolder> holderList = getEffectHoldersOfFlask(stack);
		if (holderList.size() <= 0)
		{
			return ActionResult.pass(stack);
		}

		world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide)
		{
			EntityPotionFlask potionentity = new EntityPotionFlask(world, player);
			potionentity.setItem(stack);
			potionentity.shootFromRotation(player, player.xRot, player.yRot, -20.0F, 0.5F, 1.0F);
			prepPotionFlask(stack, player, potionentity);
			world.addFreshEntity(potionentity);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.abilities.instabuild)
		{
			stack.setDamageValue(stack.getDamageValue() + 1);
		}

		return ActionResult.sidedSuccess(stack, world.isClientSide());
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
		Collection<EffectInstance> instanceList = PotionUtils.getMobEffects(stack);

		int color = instanceList.isEmpty() ? PotionUtils.getColor(Potions.WATER)
				: PotionUtils.getColor(instanceList);
		stack.getTag().putInt("CustomPotionColor", color);
	}
}
