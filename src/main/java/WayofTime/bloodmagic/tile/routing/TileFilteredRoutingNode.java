package WayofTime.bloodmagic.tile.routing;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.api.Constants;

public class TileFilteredRoutingNode extends TileRoutingNode implements ISidedInventory
{
    public int currentActiveSlot = 0;
    public int[] priorities = new int[6];

    public TileFilteredRoutingNode(int size, String name)
    {
        super(size, name);
    }

    public ItemStack getFilterStack(EnumFacing side)
    {
        int index = side.getIndex();
        if (currentActiveSlot == index)
        {
            return getStackInSlot(0);
        } else
        {
            return getStackInSlot(index + 1);
        }
    }

    @Override
    public boolean isInventoryConnectedToSide(EnumFacing side)
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        currentActiveSlot = tag.getInteger("currentSlot");
        priorities = tag.getIntArray(Constants.NBT.ROUTING_PRIORITY);
        if (priorities.length != 6)
        {
            priorities = new int[6];
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("currentSlot", currentActiveSlot);
        tag.setIntArray(Constants.NBT.ROUTING_PRIORITY, priorities);
    }

    public void swapFilters(int requestedSlot)
    {
        this.setInventorySlotContents(currentActiveSlot + 1, this.getStackInSlot(0));
        this.setInventorySlotContents(0, this.getStackInSlot(requestedSlot + 1));
        this.setInventorySlotContents(requestedSlot + 1, null);
        currentActiveSlot = requestedSlot;
        this.markDirty();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }

    @Override
    public int getPriority(EnumFacing side)
    {
        return priorities[side.getIndex()];
    }

    public void incrementCurrentPriotiryToMaximum(int max)
    {
        priorities[currentActiveSlot] = Math.min(priorities[currentActiveSlot] + 1, max);
    }

    public void decrementCurrentPriority()
    {
        priorities[currentActiveSlot] = Math.max(priorities[currentActiveSlot] - 1, 0);
    }
}
