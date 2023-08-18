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

public class BasicFluidFilter implements IFluidFilter
{
	protected List<IFilterKey> requestList;
	protected BlockEntity accessedTile;
	protected IFluidHandler fluidHandler;

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
				filterStack.setCount(filterStack.getCount() * -1);
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

		requestList.removeIf(IFilterKey::isEmpty);
	}

	@Override
	public FluidStack transferStackThroughOutputFilter(IFluidHandler inputFluidHandler, FluidStack inputStack)
	{
		int allowedAmount = 0;
		for (IFilterKey filterStack : requestList)
		{
			if (doStacksMatch(filterStack, inputStack))
			{
				allowedAmount = Math.min(filterStack.getCount(), inputStack.getAmount());
				break;
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
			if (doStacksMatch(filterStack, inputStack))
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
	public int transferThroughInputFilter(IFluidFilter outputFilter, int maxTransfer)
	{
		int totalChange = 0;

		for (int tank = 0; tank < fluidHandler.getTanks(); tank++)
		{
			FluidStack inputStack = fluidHandler.getFluidInTank(tank);
			if (inputStack.isEmpty() || fluidHandler.drain(inputStack, FluidAction.SIMULATE).isEmpty())
			{
				continue;
			}

			int allowedAmount = 0;
			for (IFilterKey filterStack : requestList)
			{
				if (doStacksMatch(filterStack, inputStack))
				{
					allowedAmount = Math.min(maxTransfer, Math.min(filterStack.getCount(), fluidHandler.drain(inputStack, FluidAction.SIMULATE).getAmount()));
					break;
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
				continue;
			}

			Iterator<IFilterKey> itr = requestList.iterator();
			while (itr.hasNext())
			{
				IFilterKey filterStack = itr.next();
				if (doStacksMatch(filterStack, inputStack))
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
				return true;
			}
		}

		return false;
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
