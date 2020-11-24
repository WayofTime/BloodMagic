package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.resultFail(stack);

		if (!world.isRemote && !isUnusable(stack))
		{
			RayTraceResult rayTrace = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

			if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK)
			{
				return ActionResult.resultFail(stack);
			}

			BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTrace;
			BlockPos blockPos = blockRayTrace.getPos();
			Direction sideHit = blockRayTrace.getFace();

			if (world.isBlockModifiable(player, blockPos) && player.canPlayerEdit(blockPos, sideHit, stack))
			{
				BlockState blockState = world.getBlockState(blockPos);
				if (blockState.getBlock() instanceof IBucketPickupHandler)
				{
					if (NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
					{
						((IBucketPickupHandler) blockState.getBlock()).pickupFluid(world, blockPos, blockState);
						return ActionResult.resultSuccess(stack);
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

		return super.onItemRightClick(world, player, hand);
	}
}