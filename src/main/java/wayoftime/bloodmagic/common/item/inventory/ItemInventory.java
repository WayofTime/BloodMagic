package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class ItemInventory implements Container
{
	protected int[] syncedSlots = new int[0];
	protected ItemStack masterStack;
	protected NonNullList<ItemStack> inventory;
	private int size;
	private String name;

	public ItemInventory(ItemStack masterStack, int size, String name)
	{
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
		this.size = size;
		this.name = name;
		this.masterStack = masterStack;

		if (!masterStack.isEmpty())
			this.readFromStack(masterStack);
	}

	public void initializeInventory(ItemStack masterStack)
	{
		this.masterStack = masterStack;
		this.clearContent();
		this.readFromStack(masterStack);
	}

	private boolean isSyncedSlot(int slot)
	{
		for (int s : this.syncedSlots)
		{
			if (s == slot)
			{
				return true;
			}
		}
		return false;
	}

	public void readFromNBT(CompoundTag tagCompound)
	{
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		ContainerHelper.loadAllItems(tagCompound, this.inventory);
	}

	public void writeToNBT(CompoundTag tagCompound)
	{
		ContainerHelper.saveAllItems(tagCompound, this.inventory);
//		ListNBT tags = new ListNBT();
//
//		for (int i = 0; i < inventory.size(); i++)
//		{
//			if ((!inventory.get(i).isEmpty()) && !isSyncedSlot(i))
//			{
//				CompoundNBT data = new CompoundNBT();
//				data.putByte(Constants.NBT.SLOT, (byte) i);
//				inventory.get(i).write(data);
//				tags.add(data);
////				tags.appendTag(data);
//			}
//		}
//
//		tagCompound.put(Constants.NBT.ITEMS, tags);
	}

	public void readFromStack(ItemStack masterStack)
	{
		if (masterStack != null)
		{
			NBTHelper.checkNBT(masterStack);
			CompoundTag tag = masterStack.getTag();
			readFromNBT(tag.getCompound(Constants.NBT.ITEM_INVENTORY + name));
		}
	}

	public void writeToStack(ItemStack masterStack)
	{
		if (masterStack != null)
		{
			NBTHelper.checkNBT(masterStack);
			CompoundTag tag = masterStack.getTag();
			CompoundTag invTag = new CompoundTag();
			writeToNBT(invTag);
			tag.put(Constants.NBT.ITEM_INVENTORY + name, invTag);
		}
	}

	@Override
	public int getContainerSize()
	{
		return size;
	}

	@Override
	public ItemStack getItem(int index)
	{
		return inventory.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count)
	{
		if (!inventory.get(index).isEmpty())
		{
//            if (!worldObj.isRemote)
//                worldObj.markBlockForUpdate(this.pos);

			if (inventory.get(index).getCount() <= count)
			{
				ItemStack itemStack = inventory.get(index);
				inventory.set(index, ItemStack.EMPTY);
				setChanged();
				return itemStack;
			}

			ItemStack itemStack = inventory.get(index).split(count);
			if (inventory.get(index).isEmpty())
				inventory.set(index, ItemStack.EMPTY);

			setChanged();
			return itemStack;
		}

		return null;
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
		if (stack.getCount() > getMaxStackSize())
			stack.setCount(getMaxStackSize());
		setChanged();
//        if (!worldObj.isRemote)
//            worldObj.markBlockForUpdate(this.pos);
	}

	@Override
	public int getMaxStackSize()
	{
		return 64;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	@Override
	public void startOpen(Player player)
	{

	}

	@Override
	public void stopOpen(Player player)
	{

	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack)
	{
		return true;
	}

//	@Override
//	public int getField(int id)
//	{
//		return 0;
//	}
//
//	@Override
//	public void setField(int id, int value)
//	{
//
//	}
//
//	@Override
//	public int getFieldCount()
//	{
//		return 0;
//	}

	@Override
	public void clearContent()
	{
		this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

//	@Override
//	public String getName()
//	{
//		return name;
//	}
//
//	@Override
//	public boolean hasCustomName()
//	{
//		return false;
//	}
//
//	@Override
//	public ITextComponent getDisplayName()
//	{
//		return new StringTextComponent(getName());
//	}

	@Override
	public void setChanged()
	{
		if (masterStack != null)
		{
			this.writeToStack(masterStack);
		}
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	public boolean canInventoryBeManipulated()
	{
		return masterStack != null;
	}
}