package wayoftime.bloodmagic.common.routing;

import java.util.Iterator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import wayoftime.bloodmagic.common.item.routing.IFilterKey;

public class BlacklistFluidFilter implements IRoutingFilter<FluidStack>
{
	protected List<IFilterKey> requestList;
	protected BlockEntity accessedTile;
	protected IFluidHandler fluidHandler;

	@Override
	public FluidStack getType()
	{
		return FluidStack.EMPTY;
	}

	@Override
	public void initializeFilter(List<IFilterKey> filteredList, BlockEntity tile, Direction side, boolean isFilterOutput)
	{
		this.accessedTile = tile;
		this.fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve().orElse(null);
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
	}

	@Override
	public FluidStack transferStackThroughOutputFilter(FluidStack inputStack)
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
		int changeAmount = fluidHandler.fill(testStack, FluidAction.EXECUTE);

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

	@Override
	public int transferThroughInputFilter(IRoutingFilter<FluidStack> outputFilter, int maxTransfer)
	{
		int totalChange = 0;
		tanks: for (int tank = 0; tank < fluidHandler.getTanks(); tank++)
		{
			FluidStack availableStack = fluidHandler.getFluidInTank(tank);
			FluidStack inputStack = fluidHandler.drain(availableStack, FluidAction.SIMULATE);
			if (inputStack.isEmpty())
			{
				continue;
			}

			int allowedAmount = Math.min(inputStack.getAmount(), maxTransfer);
			for (IFilterKey filterStack : requestList)
			{
				if (doStacksMatch(filterStack, inputStack))
				{
					continue tanks;
				}
			}

			if (allowedAmount <= 0)
			{
				continue;
			}

			FluidStack testStack = inputStack.copy();
			testStack.setAmount(allowedAmount);
			FluidStack remainderStack = outputFilter.transferStackThroughOutputFilter(testStack);
			int changeAmount = allowedAmount - (remainderStack.isEmpty() ? 0 : remainderStack.getAmount());

			if (!remainderStack.isEmpty() && remainderStack.getAmount() == allowedAmount)
			{
				continue;
			}

			fluidHandler.drain(changeAmount, FluidAction.EXECUTE);

			Iterator<IFilterKey> itr = requestList.iterator();
			while (itr.hasNext())
			{
				IFilterKey filterStack = itr.next();
				if (!doStacksMatch(filterStack, inputStack))
				{
					filterStack.shrink(changeAmount);
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
		return filterStack.doesStackMatch(testStack);
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
