package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilVoid extends ItemSigilFluidBase
{
	public ItemSigilVoid()
	{
		super("void", 50, new FluidStack(Fluids.EMPTY, 1000));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		if (!world.isClientSide && !isUnusable(stack))
		{
			HitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

			if (rayTrace == null || rayTrace.getType() != HitResult.Type.BLOCK)
			{
				return InteractionResultHolder.fail(stack);
			}

			BlockHitResult blockRayTrace = (BlockHitResult) rayTrace;
			BlockPos blockPos = blockRayTrace.getBlockPos();
			Direction sideHit = blockRayTrace.getDirection();

			if (world.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos, sideHit, stack))
			{
				BlockState blockState = world.getBlockState(blockPos);
				if (blockState.getBlock() instanceof BucketPickup)
				{
					if (NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
					{
						((BucketPickup) blockState.getBlock()).pickupBlock(world, blockPos, blockState);
						return InteractionResultHolder.success(stack);
					}
				}
				// Void is simpler than the other fluid sigils, because getFluidHandler grabs
				// fluid blocks just fine
				// So extract from fluid tanks with a null side; or drain fluid blocks.
//				IFluidHandler destination = getFluidHandler(world, blockPos, sideHit);
//				if (destination != null && tryRemoveFluid(destination, 1000, false)
//						&& NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
//				{
//					if (tryRemoveFluid(destination, 1000, true))
//						return ActionResult.resultSuccess(stack);
//				}
//				// Do the same as above, but use sidedness to interact with the fluid handler.
//				IFluidHandler destinationSide = getFluidHandler(world, blockPos, sideHit);
//				if (destinationSide != null && tryRemoveFluid(destinationSide, 1000, false)
//						&& NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
//				{
//					if (tryRemoveFluid(destinationSide, 1000, true))
//						return ActionResult.resultSuccess(stack);
//				}
			}
		}

		return super.use(world, player, hand);
	}
}