package wayoftime.bloodmagic.common.routing;

import java.util.Iterator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import wayoftime.bloodmagic.common.item.routing.IFilterKey;

/**
 * This particular implementation of IItemFilter checks to make sure that a) as
 * an output filter it will fill until the requested amount and b) as an input
 * filter it will only syphon until the requested amount.
 *
 * @author WayofTime
 */
public class BlacklistFluidFilter implements IFluidFilter
{
	/*
	 * This list acts as the way the filter keeps track of its contents. For the
	 * case of an output filter, it holds a list of ItemStacks that needs to be
	 * inserted in the inventory to finish its request. For the case of an input
	 * filter, it keeps track of how many can be removed.
	 */
	protected List<IFilterKey> requestList;
	protected BlockEntity accessedTile;
	protected IFluidHandler fluidHandler;

	/**
	 * Initializes the filter so that it knows what it wants to fulfill.
	 *
	 * @param filteredList   - The list of ItemStacks that the filter is set to.
	 * @param tile           - The inventory that is being accessed. This inventory
	 *                       is either being pulled from or pushed to.
	 * @param fluidHandler   - The item handler
	 * @param isFilterOutput - Tells the filter what actions to expect. If true, it
	 *                       should be initialized as an output filter. If false, it
	 *                       should be initialized as an input filter.
	 */
	@Override
	public void initializeFilter(List<IFilterKey> filteredList, BlockEntity tile, IFluidHandler fluidHandler, boolean isFilterOutput)
	{
		this.accessedTile = tile;
		this.fluidHandler = fluidHandler;
		if (isFilterOutput)
		{
			requestList = filteredList;

			for (int tank = 0; tank < fluidHandler.getTanks(); tank++)
			{
				FluidStack checkedStack = fluidHandler.getFluidInTank(tank);
				if (checkedStack.isEmpty())
				{
					continue;
				}

				int stackSize = checkedStack.getAmount();

				for (IFilterKey filterStack : requestList)
				{
					if (filterStack.getCount() == 0)
					{
						continue;
					}

					if (doStacksMatch(filterStack, checkedStack))
					{
						filterStack.setCount(Math.max(filterStack.getCount() - stackSize, 0));
						continue;
					}
				}
			}
		} else
		{
			requestList = filteredList;
			for (IFilterKey filterStack : requestList)
			{
				filterStack.setCount(filterStack.getCount() * -1); // Invert the stack size so that
			}

			for (int tank = 0; tank < fluidHandler.getTanks(); tank++)
			{
				FluidStack checkedStack = fluidHandler.getFluidInTank(tank);
				if (checkedStack.isEmpty())
				{
					continue;
				}

				int stackSize = checkedStack.getAmount();

				for (IFilterKey filterStack : filteredList)
				{
					if (doStacksMatch(filterStack, checkedStack))
					{
						filterStack.grow(stackSize);
						continue;
					}
				}
			}
		}

		// Might not want to remove items from the "ignore list" if their item needs are
		// met.
//		requestList.removeIf(IFilterKey::isEmpty);
	}

	/**
	 * This method is only called when the output inventory this filter is managing
	 * receives an ItemStack. Should only really be called by the Input filter via
	 * it's transfer method.
	 *
	 * @param inputStack - The stack to transfer
	 * @return - The remainder of the stack after it has been absorbed into the
	 *         inventory.
	 */
	@Override
	public FluidStack transferStackThroughOutputFilter(IFluidHandler inputFluidHandler, FluidStack inputStack)
	{
		int allowedAmount = inputStack.getAmount();
		for (IFilterKey filterStack : requestList)
		{
			if (doStacksMatch(filterStack, inputStack))
			{
				// If the stacks match, then we don't want to pull this item at all.
				return inputStack;
			}
		}

		if (allowedAmount <= 0)
		{
			return inputStack;
		}

		FluidStack testStack = inputStack.copy();
		testStack.setAmount(allowedAmount);
		FluidStack remainderStack = FluidUtil.tryFluidTransfer(fluidHandler, inputFluidHandler, testStack, true);

		int changeAmount = allowedAmount - (remainderStack.isEmpty() ? 0 : remainderStack.getAmount());
		testStack = inputStack.copy();
		testStack.shrink(changeAmount);

		Iterator<IFilterKey> itr = requestList.iterator();
		while (itr.hasNext())
		{
			IFilterKey filterStack = itr.next();
			if (!doStacksMatch(filterStack, inputStack))
			{
				filterStack.shrink(changeAmount);
				if (filterStack.isEmpty())
				{
					itr.remove();
				}
			}
		}

		Level world = accessedTile.getLevel();
		BlockPos pos = accessedTile.getBlockPos();
		world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

		return testStack;
	}

	/**
	 * This method is only called on an input filter to transfer ItemStacks from the
	 * input inventory to the output inventory.
	 */
	@Override
	public int transferThroughInputFilter(IFluidFilter outputFilter, int maxTransfer)
	{
		int totalChange = 0;
		slots: for (int slot = 0; slot < fluidHandler.getTanks(); slot++)
		{
			FluidStack inputStack = fluidHandler.getFluidInTank(slot);
			if (inputStack.isEmpty() || fluidHandler.drain(inputStack, FluidAction.SIMULATE).isEmpty())
			{
				continue;
			}

			int allowedAmount = Math.min(inputStack.getAmount(), maxTransfer);
			for (IFilterKey filterStack : requestList)
			{
				if (doStacksMatch(filterStack, inputStack))
				{
					// They matched. That is not good. Bail!
					continue slots;
				}
			}

			if (allowedAmount <= 0)
			{
				continue;
			}

			FluidStack testStack = inputStack.copy();
			testStack.setAmount(allowedAmount);
			FluidStack remainderStack = outputFilter.transferStackThroughOutputFilter(fluidHandler, testStack);
			int changeAmount = allowedAmount - (remainderStack.isEmpty() ? 0 : remainderStack.getAmount());

			if (!remainderStack.isEmpty() && remainderStack.getAmount() == allowedAmount)
			{
				// Nothing has changed. Moving on!
				continue;
			}

			Iterator<IFilterKey> itr = requestList.iterator();
			while (itr.hasNext())
			{
				IFilterKey filterStack = itr.next();
				if (!doStacksMatch(filterStack, inputStack))
				{
					filterStack.shrink(changeAmount);
//					if (filterStack.isEmpty())
//					{
//						itr.remove();
//					}
				}
			}

			Level world = accessedTile.getLevel();
			BlockPos pos = accessedTile.getBlockPos();
			world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

			maxTransfer -= changeAmount;
			totalChange += changeAmount;
			if (maxTransfer <= 0)
			{
				return totalChange;
			}
		}

		return totalChange;
	}

	@Override
	public boolean doesStackPassFilter(FluidStack testStack)
	{
		for (IFilterKey filterStack : requestList)
		{
			if (doStacksMatch(filterStack, testStack))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean doStacksMatch(IFilterKey filterStack, FluidStack testStack)
	{
		return filterStack.doesStackMatch(new ItemStack(testStack.getRawFluid().getBucket()));
	}

	@Override
	public void initializeFilter(List<IFilterKey> filteredList)
	{
		this.requestList = filteredList;
	}

	@Override
	public List<IFilterKey> getFilterList()
	{
		return this.requestList;
	}
}
