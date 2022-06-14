package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class InventoryWrapper implements Container
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
	public void clearContent()
	{
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	@Override
	public int getContainerSize()
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
	public ItemStack getItem(int index)
	{
		return inventory.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count)
	{
		if (!getItem(index).isEmpty())
		{
			if (getItem(index).getCount() <= count)
			{
				ItemStack itemStack = inventory.get(index);
				inventory.set(index, ItemStack.EMPTY);
				setChanged();
				return itemStack;
			}

			ItemStack itemStack = inventory.get(index).split(count);
			setChanged();
			return itemStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		if (!inventory.get(slot).isEmpty())
		{
			ItemStack itemStack = inventory.get(slot);
			setItem(slot, ItemStack.EMPTY);
			return itemStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		inventory.set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > getMaxStackSize())
			stack.setCount(getMaxStackSize());
		setChanged();
	}

	@Override
	public void setChanged()
	{

	}

	@Override
	public boolean stillValid(Player player)
	{
		return false;
	}

}
