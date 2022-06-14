package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import wayoftime.bloodmagic.common.item.sigil.ISigil.Holding;

public class ItemSigilAir extends ItemSigilBase
{
	public ItemSigilAir()
	{
		super("air", 50);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		boolean unusable = isUnusable(stack);
		if (world.isClientSide && !unusable)
		{
			Vec3 vec = player.getLookAngle();
			double wantedVelocity = 1.7;

			// TODO - Revisit after potions
//			if (player.isPotionActive(RegistrarBloodMagic.BOOST))
//			{
//				int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
//				wantedVelocity += (1 + amplifier) * (0.35);
//			}

			player.setDeltaMovement(vec.x * wantedVelocity, vec.y * wantedVelocity, vec.z * wantedVelocity);
		}

		world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

		if (!world.isClientSide)
		{
			if (!player.isCreative())
				this.setUnusable(stack, !NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess());

			if (!unusable)
				player.fallDistance = 0;
		}

		return super.use(world, player, hand);
	}
}