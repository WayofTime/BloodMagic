package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.util.GhostItemHelper;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

public class TileFilteredRoutingNode extends TileRoutingNode implements ISidedInventory {
    public int currentActiveSlot = 0;
    public int[] priorities = new int[6];

    public ItemInventory itemInventory = new ItemInventory(ItemStack.EMPTY, 9, "");

    public TileFilteredRoutingNode(int size, String name) {
        super(size, name);
    }

    public ItemStack getFilterStack(Direction side) {
        int index = side.getIndex();

        return getStackInSlot(index);
    }

    public void setGhostItemAmount(int ghostItemSlot, int amount) {
        ItemStack stack = itemInventory.getStackInSlot(ghostItemSlot);
        if (!stack.isEmpty()) {
            GhostItemHelper.setItemGhostAmount(stack, amount);
        }

        this.markDirty();
    }

    @Override
    public boolean isInventoryConnectedToSide(Direction side) {
        return true;
    }

    @Override
    public void deserialize(CompoundNBT tag) {
        super.deserialize(tag);
        currentActiveSlot = tag.getInt("currentSlot");
        priorities = tag.getIntArray(Constants.NBT.ROUTING_PRIORITY);
        if (priorities.length != 6) {
            priorities = new int[6];
        }

        if (!tag.getBoolean("updated")) {
            ListNBT tags = tag.getList("Items", 10);
            inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
            for (int i = 0; i < tags.tagCount(); i++) {
                if (!isSyncedSlot(i)) {
                    CompoundNBT data = tags.getCompound(i);
                    byte j = data.getByte("Slot");

                    if (j == 0) {
                        inventory.set(i, new ItemStack(data));
                    } else if (j >= 1 && j < inventory.size() + 1) {
                        inventory.set(j - 1, new ItemStack(data));
                    }
                }
            }
        }

        itemInventory = new ItemInventory(getStackInSlot(currentActiveSlot), 9, "");
    }

    @Override
    public CompoundNBT serialize(CompoundNBT tag) {
        super.serialize(tag);
        tag.putInt("currentSlot", currentActiveSlot);
        tag.putIntArray(Constants.NBT.ROUTING_PRIORITY, priorities);
        tag.putBoolean("updated", true);
        return tag;
    }

    public void swapFilters(int requestedSlot) {
        currentActiveSlot = requestedSlot;
        itemInventory.initializeInventory(getStackInSlot(currentActiveSlot));
        this.markDirty();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int getPriority(Direction side) {
        return priorities[side.getIndex()];
    }

    public void incrementCurrentPriotiryToMaximum(int max) {
        priorities[currentActiveSlot] = Math.min(priorities[currentActiveSlot] + 1, max);
        BlockState state = getWorld().getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }

    public void decrementCurrentPriority() {
        priorities[currentActiveSlot] = Math.max(priorities[currentActiveSlot] - 1, 0);
        BlockState state = getWorld().getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }
}
