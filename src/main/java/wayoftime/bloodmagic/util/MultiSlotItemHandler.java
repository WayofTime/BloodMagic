package wayoftime.bloodmagic.util;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class MultiSlotItemHandler implements IItemHandler
{
	private ItemStack[] items;

	private final int invLimit;

	public MultiSlotItemHandler(int size, int invLimit)
	{
		items = new ItemStack[size];
		for (int i = 0; i < size; i++)
		{
			items[i] = ItemStack.EMPTY;
		}

		this.invLimit = invLimit;
	}

	public MultiSlotItemHandler(ItemStack[] items, int invLimit)
	{
		this.items = items;
		this.invLimit = invLimit;
	}

	@Override
	public int getSlots()
	{
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return items[slot];
	}

	public boolean isItemValid(int slot, ItemStack stack)
	{
		return true;
	}

	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		items[slot] = stack;
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getStackInSlot(slot);

		int m;
		if (!stackInSlot.isEmpty())
		{
			if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
				return stack;

			if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
				return stack;

			if (!isItemValid(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

			if (stack.getCount() <= m)
			{
				if (!simulate)
				{
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot, copy);
				}

				return ItemStack.EMPTY;
			} else
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate)
				{
					ItemStack copy = stack.split(m);
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot, copy);
					return stack;
				} else
				{
					stack.shrink(m);
					return stack;
				}
			}
		} else
		{
			if (!isItemValid(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
			if (m < stack.getCount())
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate)
				{
					setInventorySlotContents(slot, stack.split(m));
					return stack;
				} else
				{
					stack.shrink(m);
					return stack;
				}
			} else
			{
				if (!simulate)
				{
					setInventorySlotContents(slot, stack);
				}
				return ItemStack.EMPTY;
			}
		}
	}

	public boolean canTransferAllItemsToSlots(List<ItemStack> stackList, boolean simulate)
	{
		ItemStack[] copyList = new ItemStack[items.length];
		for (int i = 0; i < copyList.length; i++)
		{
			copyList[i] = items[i].copy();
		}

		boolean hasStashedAll = true;

		for (ItemStack stack : stackList)
		{
			if (stack.isEmpty())
			{
				continue;
			}

			slots: for (int slot = 0; slot < copyList.length; slot++)
			{
				ItemStack stackInSlot = copyList[slot];

				int m;
				if (!stackInSlot.isEmpty())
				{
					if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
						continue;

					if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
						continue;

					if (!isItemValid(slot, stack))
						continue;

					m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

					if (stack.getCount() <= m)
					{
						ItemStack copy = stack.copy();
						if (!simulate)
						{
							copy.grow(stackInSlot.getCount());
							copyList[slot] = copy;
						}
						stack = ItemStack.EMPTY;

						break slots;
					} else
					{
						// copy the stack to not modify the original one
						stack = stack.copy();
						if (!simulate)
						{
							ItemStack copy = stack.split(m);
							copy.grow(stackInSlot.getCount());
							copyList[slot] = copy;
						} else
						{
							stack.shrink(m);
						}
					}
				} else
				{
					if (!isItemValid(slot, stack))
						continue;

					m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
					if (m < stack.getCount())
					{
						// copy the stack to not modify the original one
						stack = stack.copy();
						if (!simulate)
						{
							copyList[slot] = stack.split(m);
						} else
						{
							stack.shrink(m);
						}
					} else
					{
						if (!simulate)
						{
							copyList[slot] = stack;
						}

						stack = ItemStack.EMPTY;
					}
				}
			}

			if (!stack.isEmpty())
			{
				hasStashedAll = false;
				break;
			}
		}

		if (!simulate)
		{
			items = copyList;
		}

		return hasStashedAll;
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getStackInSlot(slot);

		if (stackInSlot.isEmpty())
			return ItemStack.EMPTY;

		if (simulate)
		{
			if (stackInSlot.getCount() < amount)
			{
				return stackInSlot.copy();
			} else
			{
				ItemStack copy = stackInSlot.copy();
				copy.setCount(amount);
				return copy;
			}
		} else
		{
			int m = Math.min(stackInSlot.getCount(), amount);

			ItemStack decrStackSize = decrStackSize(slot, m);
			return decrStackSize;
		}
	}

	public ItemStack decrStackSize(int slot, int amount)
	{
		if (!getStackInSlot(slot).isEmpty())
		{
			if (getStackInSlot(slot).getCount() <= amount)
			{
				ItemStack itemStack = getStackInSlot(slot);
				setInventorySlotContents(slot, ItemStack.EMPTY);
				return itemStack;
			}

			ItemStack itemStack = getStackInSlot(slot).split(amount);
			return itemStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return invLimit;
	}

}
