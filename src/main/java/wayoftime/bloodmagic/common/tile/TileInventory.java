package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import wayoftime.bloodmagic.common.tile.base.TileBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileInventory extends TileBase implements Container
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

	public TileInventory(BlockEntityType<?> type, int size, String name, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
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
	public void deserialize(CompoundTag tagCompound)
	{
		super.deserialize(tagCompound);

		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		ContainerHelper.loadAllItems(tagCompound, this.inventory);

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
	public CompoundTag serialize(CompoundTag tagCompound)
	{
		super.serialize(tagCompound);

		ContainerHelper.saveAllItems(tagCompound, this.inventory);
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
		Containers.dropContents(getLevel(), getBlockPos(), this);
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
		if (!getItem(index).isEmpty())
		{
			if (!getLevel().isClientSide)
				getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);

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
		if (!getLevel().isClientSide)
			getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
	}

	@Override
	public int getMaxStackSize()
	{
		return 64;
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
	public void clearContent()
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
	public boolean stillValid(Player player)
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
//		return Component.literalString(getName());
//	}

	protected void initializeItemHandlers()
	{
		if (this instanceof WorldlyContainer)
		{
			handlerDown = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.DOWN));
			handlerUp = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.UP));
			handlerNorth = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.NORTH));
			handlerSouth = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.SOUTH));
			handlerWest = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.WEST));
			handlerEast = LazyOptional.of(() -> new SidedInvWrapper((WorldlyContainer) this, Direction.EAST));
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
		if (facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
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
		} else if (facing == null && capability == ForgeCapabilities.ITEM_HANDLER)
		{
			return handlerDown.cast();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps()
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
//		return capability == ForgeCapabilities.ITEM_HANDLER || super.hasCapability(capability, facing);
//	}
}