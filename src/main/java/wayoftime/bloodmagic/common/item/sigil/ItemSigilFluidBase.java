package wayoftime.bloodmagic.common.item.sigil;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;

public abstract class ItemSigilFluidBase extends ItemSigilBase
{
	// Class for sigils that interact with fluids, either creating or deleting them.
	// Sigils still have to define their own onRightClick behavior, but the actual
	// fluid-interacting code is largely limited to here.
	public final FluidStack sigilFluid;

	public ItemSigilFluidBase(String name, int lpUsed, FluidStack fluid)
	{
		super(name, lpUsed);
		sigilFluid = fluid;
	}

	public ItemSigilFluidBase(String name, FluidStack fluid)
	{
		super(name);
		sigilFluid = fluid;
	}

	public ItemSigilFluidBase(String name)
	{
		super(name);
		sigilFluid = null;
	}

	// The following are handler functions for fluids, all genericized.
	// They're all based off of the Forge FluidUtil methods, but directly taking the
	// sigilFluid constant instead of getting an argument.

	/*
	 * Gets a fluid handler for the targeted block and siding. Works for both tile
	 * entity liquid containers and fluid blocks. This one is literally identical to
	 * the FluidUtil method of the same signature.
	 */
	@Nullable
	protected IFluidHandler getFluidHandler(Level world, BlockPos blockPos, @Nullable Direction side)
	{
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();

		IFluidHandler targetFluidHandler = FluidUtil.getFluidHandler(world, blockPos, side).orElse(null);

		if (targetFluidHandler == null)

		{

		}
		return targetFluidHandler;
//		if (block instanceof IFluidBlock)
//			return new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
//		else if (block instanceof BlockLiquid)
//			return new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
//		return null;
	}

	/*
	 * Tries to insert fluid into a fluid handler. If doTransfer is false, only
	 * simulate the transfer. If true, actually do so. Returns true if the transfer
	 * is successful, false otherwise.
	 */
	protected boolean tryInsertSigilFluid(IFluidHandler destination, boolean doTransfer)
	{
		if (destination == null)
			return false;
		return destination.fill(sigilFluid, doTransfer ? FluidAction.EXECUTE : FluidAction.SIMULATE) > 0;
	}

	/*
	 * Tries basically the oppostive of the above, removing fluids instead of adding
	 * them
	 */
	protected boolean tryRemoveFluid(IFluidHandler source, int amount, boolean doTransfer)
	{
		if (source == null)
			return false;
		return source.drain(amount, doTransfer ? FluidAction.EXECUTE : FluidAction.SIMULATE) != null;
	}

	/*
	 * Tries to place a fluid block in the world. Returns true if successful,
	 * otherwise false. This is the big troublesome one, oddly enough. It's
	 * genericized in case anyone wants to create variant sigils with weird fluids.
	 */
	protected boolean tryPlaceSigilFluid(Player player, Level world, BlockPos blockPos)
	{
		FluidStack resource = sigilFluid;
		BlockState state = sigilFluid.getFluid().getFluidType().getBlockForFluidState(world, blockPos, sigilFluid.getFluid().defaultFluidState());
		BlockWrapper wrapper = new BlockWrapper(state, world, blockPos);

		if (world.dimensionType().ultraWarm() && resource.getFluid().getFluidType().isVaporizedOnPlacement(world, blockPos, resource))
		{
			resource.getFluid().getFluidType().onVaporize(player, world, blockPos, resource);
			return true;
		}

		return wrapper.fill(sigilFluid, FluidAction.EXECUTE) > 0;
//		// Make sure world coordinants are valid
//		if (world == null || blockPos == null)
//		{
//			return false;
//		}
//		// Make sure fluid is placeable
//		Fluid fluid = sigilFluid.getFluid();
//		if (!fluid.getAttributes().canBePlacedInWorld(world, blockPos, sigilFluid))
//		{
//			return false;
//		}
//
//		// Check if the block is an air block or otherwise replaceable
//		BlockState state = world.getBlockState(blockPos);
//		Material mat = state.getMaterial();
//		boolean isDestSolid = mat.isSolid();
//		boolean isDestReplaceable = state.getBlock().isReplaceable(state, fluid);
//		if (!world.isAirBlock(blockPos) && isDestSolid && !isDestReplaceable)
//		{
//			return false;
//		}
//
////		// If the fluid vaporizes, this exists here in the lava sigil solely so the code
////		// is usable for other fluids
////		if (world.provider.doesWaterVaporize() && fluid.doesVaporize(sigilFluid))
////		{
////			fluid.vaporize(player, world, blockPos, sigilFluid);
////			return true;
////		}
//
//		// Finally we've done enough checking to make sure everything at the end is
//		// safe, let's place some fluid.
//		IFluidHandler handler;
//		Block block = fluid.getAttributes().getStateForPlacement(world, blockPos, sigilFluid).getBlockState().getBlock();
//		if (block instanceof IFluidBlock)
//		{
//			handler = new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
//		} else if (block instanceof BlockLiquid)
//			handler = new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
//		else
//			handler = new BlockWrapper(block, world, blockPos);
//		return tryInsertSigilFluid(handler, true);
////		return false;
	}
}