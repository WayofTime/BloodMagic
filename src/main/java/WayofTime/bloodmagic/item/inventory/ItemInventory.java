package WayofTime.bloodmagic.item.inventory;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagList tags = tagCompound.getTagList(Constants.NBT.ITEMS, 10);
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < tags.tagCount(); i++)
        {
            if (!isSyncedSlot(i))
            {
                NBTTagCompound data = tags.getCompoundTagAt(i);
                byte j = data.getByte(Constants.NBT.SLOT);

                if (j >= 0 && j < inventory.length)
                {
                    inventory[j] = new ItemStack(data);
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
                data.setByte(Constants.NBT.SLOT, (byte) i);
                inventory[i].writeToNBT(data);
                tags.appendTag(data);
            }
        }

        tagCompound.setTag(Constants.NBT.ITEMS, tags);
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
        if (!inventory[index].isEmpty())
        {
//            if (!worldObj.isRemote)
//                worldObj.markBlockForUpdate(this.pos);

            if (inventory[index].getCount() <= count)
            {
                ItemStack itemStack = inventory[index];
                inventory[index] = ItemStack.EMPTY;
                markDirty();
                return itemStack;
            }

            ItemStack itemStack = inventory[index].splitStack(count);
            if (inventory[index].isEmpty())
                inventory[index] = ItemStack.EMPTY;

            markDirty();
            return itemStack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (!inventory[slot].isEmpty())
        {
            ItemStack itemStack = inventory[slot];
            setInventorySlotContents(slot, ItemStack.EMPTY);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
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
    public boolean isUsableByPlayer(EntityPlayer player)
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
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(getName());
    }

    @Override
    public void markDirty()
    {
        if (masterStack != null)
        {
            this.writeToStack(masterStack);
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public boolean canInventoryBeManipulated()
    {
        return masterStack != null;
    }
}
