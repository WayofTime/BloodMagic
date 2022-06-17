package wayoftime.bloodmagic.common.item.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.entity.projectile.EntityPotionFlask;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class ItemAlchemyFlaskThrowable extends ItemAlchemyFlask
{
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.NONE;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (getRemainingUses(stack) <= 0)
		{
			return InteractionResultHolder.pass(stack);
		}

		List<EffectHolder> holderList = getEffectHoldersOfFlask(stack);
		if (holderList.size() <= 0)
		{
			return InteractionResultHolder.pass(stack);
		}

		world.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide)
		{
			EntityPotionFlask potionentity = new EntityPotionFlask(world, player);
			potionentity.setItem(stack);
			potionentity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
			prepPotionFlask(stack, player, potionentity);
			world.addFreshEntity(potionentity);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild)
		{
			stack.setDamageValue(stack.getDamageValue() + 1);
		}

		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}

	public void prepPotionFlask(ItemStack stack, Player player, EntityPotionFlask potionEntity)
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
		List<MobEffectInstance> effectList = new ArrayList<MobEffectInstance>();

		for (EffectHolder holder : holderList)
		{
			effectList.add(holder.getEffectInstance(durationModifier, false, true));
		}

		setEffectsOfFlask(stack, effectList);
		Collection<MobEffectInstance> instanceList = PotionUtils.getMobEffects(stack);

		int color = instanceList.isEmpty() ? PotionUtils.getColor(Potions.WATER) : PotionUtils.getColor(instanceList);
		stack.getTag().putInt("CustomPotionColor", color);
	}
}
