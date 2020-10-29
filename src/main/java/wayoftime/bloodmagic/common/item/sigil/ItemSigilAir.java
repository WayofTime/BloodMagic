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
import wayoftime.bloodmagic.iface.ISigil;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilAir extends ItemSigilBase
{
	public ItemSigilAir()
	{
		super("air", 50);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.resultFail(stack);

		boolean unusable = isUnusable(stack);
		if (world.isRemote && !unusable)
		{
			Vector3d vec = player.getLookVec();
			double wantedVelocity = 1.7;

			// TODO - Revisit after potions
//			if (player.isPotionActive(RegistrarBloodMagic.BOOST))
//			{
//				int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
//				wantedVelocity += (1 + amplifier) * (0.35);
//			}

			player.setMotion(vec.x * wantedVelocity, vec.y * wantedVelocity, vec.z * wantedVelocity);

			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F
					+ (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}

		if (!world.isRemote)
		{
			if (!player.isCreative())
				this.setUnusable(stack, !NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess());

			if (!unusable)
				player.fallDistance = 0;
		}

		return super.onItemRightClick(world, player, hand);
	}
}