package wayoftime.bloodmagic.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import wayoftime.bloodmagic.tile.base.TileBase;

public class TileInventory extends TileBase implements IInventory
{
	protected int[] syncedSlots = new int[0];
	protected NonNullList<ItemStack> inventory;
	LazyOptional<IItemHandler> handlerDown;
	LazyOptional<IItemHandler> handlerUp;
	LazyOptional<IItemHandler> handlerNorth;
	LazyOptional<IItemHandler> handlerSouth;
	LazyOptional<IItemHandler> handlerWest;
	LazyOptional<IItemHandler> handlerEast;
	private int size;

	// IInventory
	private String name;

	public TileInventory(TileEntityType<?> type, int size, String name)
	{
		super(type);
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
		this.size = size;
		this.name = name;
		initializeItemHandlers();
	}

	protected boolean isSyncedSlot(int slot)
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

	@Override
	public void deserialize(CompoundNBT tagCompound)
	{
		super.deserialize(tagCompound);

		this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

		ItemStackHelper.loadAllItems(tagCompound, this.inventory);

//		ListNBT tags = tagCompound.getList("Items", 10);
//		inventory = NonNullList.withSize(size, ItemStack.EMPTY);
//
//		
//		
//		for (int i = 0; i < tags.size(); i++)
//		{
//			if (!isSyncedSlot(i))
//			{
//				CompoundNBT data = tags.getCompoundTagAt(i);
//				byte j = data.getByte("Slot");
//
//				if (j >= 0 && j < inventory.size())
//				{
//					inventory.set(j, new ItemStack(data)); // No matter how much an i looks like a j, it is not one.
//															// They are drastically different characters and cause
//															// drastically different things to happen. Apparently I
//															// didn't know this at one point. - TehNut
//				}
//			}
//		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tagCompound)
	{
		super.serialize(tagCompound);

		ItemStackHelper.saveAllItems(tagCompound, this.inventory);
//		NBTTagList tags = new NBTTagList();
//
//		for (int i = 0; i < inventory.size(); i++)
//		{
//			if ((!inventory.get(i).isEmpty()) && !isSyncedSlot(i))
//			{
//				CompoundNBT data = new CompoundNBT();
//				data.putByte("Slot", (byte) i);
//				inventory.get(i).write(data);
//				tags.appendTag(data);
//			}
//		}
//
//		tagCompound.setTag("Items", tags);
		return tagCompound;
	}

	public void dropItems()
	{
		InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
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
		if (!getStackInSlot(index).isEmpty())
		{
			if (!getWorld().isRemote)
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

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
		if (!getWorld().isRemote)
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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

	// IWorldNameable

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
		this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack stack : inventory) if (!stack.isEmpty())
			return false;

		return true;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}

//	@Override
//	public String getName()
//	{
//		return TextHelper.localize("tile.bloodmagic." + name + ".name");
//	}
//
//	@Override
//	public boolean hasCustomName()
//	{
//		return true;
//	}
//
//	@Override
//	public ITextComponent getDisplayName()
//	{
//		return new TextComponentString(getName());
//	}

	protected void initializeItemHandlers()
	{
		if (this instanceof ISidedInventory)
		{
			handlerDown = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.DOWN));
			handlerUp = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.UP));
			handlerNorth = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.NORTH));
			handlerSouth = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.SOUTH));
			handlerWest = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.WEST));
			handlerEast = LazyOptional.of(() -> new SidedInvWrapper((ISidedInventory) this, Direction.EAST));
		} else
		{
			handlerDown = LazyOptional.of(() -> new InvWrapper(this));
			handlerUp = handlerDown;
			handlerNorth = handlerDown;
			handlerSouth = handlerDown;
			handlerWest = handlerDown;
			handlerEast = handlerDown;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			switch (facing)
			{
			case DOWN:
				return handlerDown.cast();
			case EAST:
				return handlerEast.cast();
			case NORTH:
				return handlerNorth.cast();
			case SOUTH:
				return handlerSouth.cast();
			case UP:
				return handlerUp.cast();
			case WEST:
				return handlerWest.cast();
			}
		} else if (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return handlerDown.cast();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	protected void invalidateCaps()
	{
		super.invalidateCaps();
		if (handlerDown != null)
			handlerDown.invalidate();

		if (handlerEast != null)
			handlerEast.invalidate();

		if (handlerNorth != null)
			handlerNorth.invalidate();

		if (handlerSouth != null)
			handlerSouth.invalidate();

		if (handlerUp != null)
			handlerUp.invalidate();

		if (handlerUp != null)
			handlerUp.invalidate();
	}

//	@Override
//	public boolean hasCapability(Capability<?> capability, Direction facing)
//	{
//		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//	}
}