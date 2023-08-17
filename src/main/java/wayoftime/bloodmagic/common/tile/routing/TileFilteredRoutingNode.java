package wayoftime.bloodmagic.common.tile.routing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.util.Constants;

public class TileFilteredRoutingNode extends TileRoutingNode implements WorldlyContainer
{
	private int currentActiveSlot = -1;
	public int[] priorities = new int[6];

//	public ItemInventory itemInventory = new ItemInventory(ItemStack.EMPTY, 9, "");

	public TileFilteredRoutingNode(BlockEntityType<?> type, int size, String name, BlockPos pos, BlockState state)
	{
		super(type, size, name, pos, state);
	}

	public ItemStack getFilterStack(Direction side)
	{
		int index = side.get3DDataValue();

		return getItem(index);
	}

//	public void setGhostItemAmount(int ghostItemSlot, int amount)
//	{
//		ItemStack stack = itemInventory.getStackInSlot(ghostItemSlot);
//		if (!stack.isEmpty())
//		{
//			GhostItemHelper.setItemGhostAmount(stack, amount);
//		}
//
//		this.markDirty();
//	}

	public int getCurrentActiveSlot()
	{
		if (currentActiveSlot == -1)
		{
			currentActiveSlot = 0;
			for (Direction dir : Direction.values())
			{
				BlockPos offsetPos = this.getCurrentBlockPos().relative(dir);
				BlockEntity tile = level.getBlockEntity(offsetPos);
				if (tile != null)
				{
					LazyOptional<IItemHandler> opt = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite());
					if (opt != null && opt.isPresent())
					{
						currentActiveSlot = dir.ordinal();
						break;
					}
				}
			}
		}

		return currentActiveSlot;
	}

	public void setCurrentActiveSlot(int slot)
	{
		this.currentActiveSlot = slot;
	}

	@Override
	public boolean isInventoryConnectedToSide(Direction side)
	{
		return true;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);
		currentActiveSlot = tag.getInt("currentSlot");
		priorities = tag.getIntArray(Constants.NBT.ROUTING_PRIORITY);
		if (priorities.length != 6)
		{
			priorities = new int[6];
		}

//		if (!tag.getBoolean("updated"))
//		{
//			NBTTagList tags = tag.getTagList("Items", 10);
//			inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
//			for (int i = 0; i < tags.tagCount(); i++)
//			{
//				if (!isSyncedSlot(i))
//				{
//					CompoundNBT data = tags.getCompoundTagAt(i);
//					byte j = data.getByte("Slot");
//
//					if (j == 0)
//					{
//						inventory.set(i, new ItemStack(data));
//					} else if (j >= 1 && j < inventory.size() + 1)
//					{
//						inventory.set(j - 1, new ItemStack(data));
//					}
//				}
//			}
//		}
//
//		itemInventory = new ItemInventory(getStackInSlot(currentActiveSlot), 9, "");
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		tag.putInt("currentSlot", currentActiveSlot);
		tag.putIntArray(Constants.NBT.ROUTING_PRIORITY, priorities);
		tag.putBoolean("updated", true);
		return tag;
	}

	public void swapFilters(int requestedSlot)
	{
		currentActiveSlot = requestedSlot;
//		itemInventory.initializeInventory(getStackInSlot(currentActiveSlot));
		this.setChanged();
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return new int[0];
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction)
	{
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		return false;
	}

	@Override
	public int getPriority(Direction side)
	{
		return priorities[side.get3DDataValue()];
	}

	public void incrementCurrentPriotiryToMaximum(int max)
	{
		priorities[currentActiveSlot] = Math.min(priorities[currentActiveSlot] + 1, max);
		BlockState state = getLevel().getBlockState(worldPosition);
		getLevel().sendBlockUpdated(worldPosition, state, state, 3);
	}

	public void decrementCurrentPriority()
	{
		priorities[currentActiveSlot] = Math.max(priorities[currentActiveSlot] - 1, 0);
		BlockState state = getLevel().getBlockState(worldPosition);
		getLevel().sendBlockUpdated(worldPosition, state, state, 3);
	}
}
