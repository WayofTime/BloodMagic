package wayoftime.bloodmagic.common.item.sigil;

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
import net.minecraftforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilLava extends ItemSigilFluidBase
{
	public ItemSigilLava()
	{
		super("lava", 1000, new FluidStack(Fluids.LAVA, 10000));
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
			RayTraceResult rayTrace = rayTrace(world, player, RayTraceContext.FluidMode.NONE);

			if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK)
			{
				return ActionResult.resultFail(stack);
			}

			BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTrace;
			BlockPos blockPos = blockRayTrace.getPos();
			Direction sideHit = blockRayTrace.getFace();
			BlockPos blockpos1 = blockPos.offset(sideHit);

			if (world.isBlockModifiable(player, blockPos) && player.canPlayerEdit(blockpos1, sideHit, stack))
			{

				// Case for if block at blockPos is a fluid handler like a tank
				// Try to put fluid into tank
				IFluidHandler destination = getFluidHandler(world, blockPos, null);
				if (destination != null && tryInsertSigilFluid(destination, false)
						&& NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					boolean result = tryInsertSigilFluid(destination, true);
					if (result)
						return ActionResult.resultSuccess(stack);
				}
				// Do the same as above, but use sidedness to interact with the fluid handler.
				IFluidHandler destinationSide = getFluidHandler(world, blockPos, sideHit);
				if (destinationSide != null && tryInsertSigilFluid(destinationSide, false)
						&& NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					boolean result = tryInsertSigilFluid(destinationSide, true);
					if (result)
						return ActionResult.resultSuccess(stack);
				}

				// Case for if block at blockPos is not a tank
				// Place fluid in world
				if (destination == null && destinationSide == null)
				{
					BlockPos targetPos = blockPos.offset(sideHit);
					if (tryPlaceSigilFluid(player, world, targetPos)
							&& NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
					{
						return ActionResult.resultSuccess(stack);
					}
				}
			}
		}

		return super.onItemRightClick(world, player, hand);
	}
}