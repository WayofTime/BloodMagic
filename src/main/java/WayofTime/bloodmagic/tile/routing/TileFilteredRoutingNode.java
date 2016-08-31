package WayofTime.bloodmagic.tile.routing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.util.GhostItemHelper;

public class TileFilteredRoutingNode extends TileRoutingNode implements ISidedInventory
{
    public int currentActiveSlot = 0;
    public int[] priorities = new int[6];

    public ItemInventory itemInventory = new ItemInventory(null, 9, "");

    public TileFilteredRoutingNode(int size, String name)
    {
        super(size, name);
    }

    public ItemStack getFilterStack(EnumFacing side)
    {
        int index = side.getIndex();

        return getStackInSlot(index);
    }

    public void setGhostItemAmount(int ghostItemSlot, int amount)
    {
        ItemStack stack = itemInventory.getStackInSlot(ghostItemSlot);
        if (stack != null)
        {
            GhostItemHelper.setItemGhostAmount(stack, amount);
        }

        this.markDirty();
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

        if (!tag.getBoolean("updated"))
        {
            NBTTagList tags = tag.getTagList("Items", 10);
            inventory = new ItemStack[getSizeInventory()];
            for (int i = 0; i < tags.tagCount(); i++)
            {
                if (!isSyncedSlot(i))
                {
                    NBTTagCompound data = tags.getCompoundTagAt(i);
                    byte j = data.getByte("Slot");

                    if (j == 0)
                    {
                        inventory[currentActiveSlot] = ItemStack.loadItemStackFromNBT(data);
                    } else if (j >= 1 && j < inventory.length + 1)
                    {
                        inventory[j - 1] = ItemStack.loadItemStackFromNBT(data);
                    }
                }
            }
        }

        itemInventory = new ItemInventory(getStackInSlot(currentActiveSlot), 9, "");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("currentSlot", currentActiveSlot);
        tag.setIntArray(Constants.NBT.ROUTING_PRIORITY, priorities);
        tag.setBoolean("updated", true);
        return tag;
    }

    public void swapFilters(int requestedSlot)
    {
        currentActiveSlot = requestedSlot;
        itemInventory.initializeInventory(getStackInSlot(currentActiveSlot));
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
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, state, state, 3);
    }

    public void decrementCurrentPriority()
    {
        priorities[currentActiveSlot] = Math.max(priorities[currentActiveSlot] - 1, 0);
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, state, state, 3);
    }
}
