package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.entity.projectile.EntityBloodLight;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilBloodLight extends ItemSigilBase
{
	public ItemSigilBloodLight()
	{
		super("bloodlight", 10);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (getCooldownRemainder(stack) > 0)
			reduceCooldown(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		HitResult mop = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

		if (getCooldownRemainder(stack) > 0)
			return super.use(world, player, hand);

		if (mop != null && mop.getType() == HitResult.Type.BLOCK)
		{
			BlockHitResult blockRayTrace = (BlockHitResult) mop;
			BlockPos blockPos = blockRayTrace.getBlockPos().relative(blockRayTrace.getDirection());

			if (world.isEmptyBlock(blockPos))
			{
				world.setBlockAndUpdate(blockPos, BloodMagicBlocks.BLOOD_LIGHT.get().defaultBlockState());
				if (!world.isClientSide)
				{
					SoulNetwork network = NetworkHelper.getSoulNetwork(getBinding(stack));
					network.syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed()));
				}
				resetCooldown(stack);
				player.swing(hand);
				return super.use(world, player, hand);
			}
		} else
		{
			if (!world.isClientSide)
			{
				SoulNetwork network = NetworkHelper.getSoulNetwork(getBinding(stack));
				EntityBloodLight light = new EntityBloodLight(world, player);
				light.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
				world.addFreshEntity(light);
				network.syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed()));
			}
			resetCooldown(stack);
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack.getItem() != newStack.getItem();
	}

	public int getCooldownRemainder(ItemStack stack)
	{
		return NBTHelper.checkNBT(stack).getTag().getInt(Constants.NBT.TICKS_REMAINING);
	}

	public void reduceCooldown(ItemStack stack)
	{
		NBTHelper.checkNBT(stack).getTag().putInt(Constants.NBT.TICKS_REMAINING, getCooldownRemainder(stack) - 1);
	}

	public void resetCooldown(ItemStack stack)
	{
		NBTHelper.checkNBT(stack).getTag().putInt(Constants.NBT.TICKS_REMAINING, 10);
	}
}
