package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
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
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.fail(stack);

		boolean unusable = isUnusable(stack);
		if (world.isClientSide && !unusable)
		{
			Vector3d vec = player.getLookAngle();
			double wantedVelocity = 1.7;

			// TODO - Revisit after potions
//			if (player.isPotionActive(RegistrarBloodMagic.BOOST))
//			{
//				int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
//				wantedVelocity += (1 + amplifier) * (0.35);
//			}

			player.setDeltaMovement(vec.x * wantedVelocity, vec.y * wantedVelocity, vec.z * wantedVelocity);
		}

		world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

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