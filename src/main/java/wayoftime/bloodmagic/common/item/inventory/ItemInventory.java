package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class ItemInventory implements IInventory
{
	protected int[] syncedSlots = new int[0];
	protected ItemStack masterStack;
	private NonNullList<ItemStack> inventory;
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
		this.clear();
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

	public void readFromNBT(CompoundNBT tagCompound)
	{
		this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

		ItemStackHelper.loadAllItems(tagCompound, this.inventory);
	}

	public void writeToNBT(CompoundNBT tagCompound)
	{
		ItemStackHelper.saveAllItems(tagCompound, this.inventory);
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
			CompoundNBT tag = masterStack.getTag();
			readFromNBT(tag.getCompound(Constants.NBT.ITEM_INVENTORY));
		}
	}

	public void writeToStack(ItemStack masterStack)
	{
		if (masterStack != null)
		{
			NBTHelper.checkNBT(masterStack);
			CompoundNBT tag = masterStack.getTag();
			CompoundNBT invTag = new CompoundNBT();
			writeToNBT(invTag);
			tag.put(Constants.NBT.ITEM_INVENTORY, invTag);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (!inventory.get(index).isEmpty())
		{
//            if (!worldObj.isRemote)
//                worldObj.markBlockForUpdate(this.pos);

			if (inventory.get(index).getCount() <= count)
			{
				ItemStack itemStack = inventory.get(index);
				inventory.set(index, ItemStack.EMPTY);
				markDirty();
				return itemStack;
			}

			ItemStack itemStack = inventory.get(index).split(count);
			if (inventory.get(index).isEmpty())
				inventory.set(index, ItemStack.EMPTY);

			markDirty();
			return itemStack;
		}

		return null;
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
		if (stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
		markDirty();
//        if (!worldObj.isRemote)
//            worldObj.markBlockForUpdate(this.pos);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void openInventory(PlayerEntity player)
	{

	}

	@Override
	public void closeInventory(PlayerEntity player)
	{

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
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
	public void clear()
	{
		this.inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
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
	public void markDirty()
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