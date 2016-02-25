package WayofTime.bloodmagic.item.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;

public class ItemInventory implements IInventory
{
    protected int[] syncedSlots = new int[0];
    private ItemStack[] inventory;
    private int size;
    private String name;
    protected ItemStack masterStack;

    public ItemInventory(ItemStack masterStack, int size, String name)
    {
        this.inventory = new ItemStack[size];
        this.size = size;
        this.name = name;
        this.masterStack = masterStack;

        if (masterStack != null)
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

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagList tags = tagCompound.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < tags.tagCount(); i++)
        {
            if (!isSyncedSlot(i))
            {
                NBTTagCompound data = tags.getCompoundTagAt(i);
                byte j = data.getByte("Slot");

                if (j >= 0 && j < inventory.length)
                {
                    inventory[j] = ItemStack.loadItemStackFromNBT(data);
                }
            }
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        NBTTagList tags = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            if ((inventory[i] != null) && !isSyncedSlot(i))
            {
                NBTTagCompound data = new NBTTagCompound();
                data.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(data);
                tags.appendTag(data);
            }
        }

        tagCompound.setTag("Items", tags);
    }

    public void readFromStack(ItemStack masterStack)
    {
        if (masterStack != null)
        {
            NBTHelper.checkNBT(masterStack);
            NBTTagCompound tag = masterStack.getTagCompound();
            readFromNBT(tag.getCompoundTag(Constants.NBT.ITEM_INVENTORY));
        }
    }

    public void writeToStack(ItemStack masterStack)
    {
        if (masterStack != null)
        {
            NBTHelper.checkNBT(masterStack);
            NBTTagCompound tag = masterStack.getTagCompound();
            NBTTagCompound invTag = new NBTTagCompound();
            writeToNBT(invTag);
            tag.setTag(Constants.NBT.ITEM_INVENTORY, invTag);
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
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        if (inventory[index] != null)
        {
//            if (!worldObj.isRemote)
//                worldObj.markBlockForUpdate(this.pos);

            if (inventory[index].stackSize <= count)
            {
                ItemStack itemStack = inventory[index];
                inventory[index] = null;
                markDirty();
                return itemStack;
            }

            ItemStack itemStack = inventory[index].splitStack(count);
            if (inventory[index].stackSize == 0)
                inventory[index] = null;

            markDirty();
            return itemStack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (inventory[slot] != null)
        {
            ItemStack itemStack = inventory[slot];
            setInventorySlotContents(slot, null);
            return itemStack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
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
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        this.inventory = new ItemStack[size];
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public IChatComponent getDisplayName()
    {
        return new ChatComponentText(getName());
    }

    @Override
    public void markDirty()
    {
        if (masterStack != null)
        {
            this.writeToStack(masterStack);
        }
    }

    public boolean canInventoryBeManipulated()
    {
        return masterStack != null;
    }
}
