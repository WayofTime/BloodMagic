package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryWrapper implements IInventory
{
	protected int[] syncedSlots = new int[0];
	protected NonNullList<ItemStack> inventory;
	private int size;

	public InventoryWrapper(int size)
	{
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
		this.size = size;
	}

	@Override
	public void clear()
	{
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack stack : inventory) if (!stack.isEmpty())
			return false;

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (!getStackInSlot(index).isEmpty())
		{
			if (getStackInSlot(index).getCount() <= count)
			{
				ItemStack itemStack = inventory.get(index);
				inventory.set(index, ItemStack.EMPTY);
				markDirty();
				return itemStack;
			}

			ItemStack itemStack = inventory.get(index).split(count);
			markDirty();
			return itemStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		if (!inventory.get(slot).isEmpty())
		{
			ItemStack itemStack = inventory.get(slot);
			setInventorySlotContents(slot, ItemStack.EMPTY);
			return itemStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory.set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
		markDirty();
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return false;
	}

}
