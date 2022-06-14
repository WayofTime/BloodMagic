package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.entity.projectile.EntityBloodLight;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import wayoftime.bloodmagic.common.item.sigil.ISigil.Holding;

public class ItemSigilBloodLight extends ItemSigilBase
{
	public ItemSigilBloodLight()
	{
		super("bloodlight", 10);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (getCooldownRemainder(stack) > 0)
			reduceCooldown(stack);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.fail(stack);

		RayTraceResult mop = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);

		if (getCooldownRemainder(stack) > 0)
			return super.use(world, player, hand);

		if (mop != null && mop.getType() == RayTraceResult.Type.BLOCK)
		{
			BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) mop;
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
				light.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
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
