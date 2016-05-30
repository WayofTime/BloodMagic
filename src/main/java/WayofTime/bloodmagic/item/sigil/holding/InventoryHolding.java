package WayofTime.bloodmagic.item.sigil.holding;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public class InventoryHolding implements IInventory
{
    public ItemStack parentItemStack;
    protected ItemStack[] inventory;

    public InventoryHolding(ItemStack itemStack)
    {
        parentItemStack = itemStack;

        inventory = new ItemStack[ItemSigilHolding.inventorySize];

        readFromNBT(itemStack.getTagCompound());
    }

    public void onGuiSaved(EntityPlayer entityPlayer)
    {
        parentItemStack = findParentItemStack(entityPlayer);

        if (parentItemStack != null)
        {
            save();
        }
    }

    public ItemStack findParentItemStack(EntityPlayer entityPlayer)
    {
        if (hasUUID(parentItemStack))
        {
            UUID parentItemStackUUID = new UUID(parentItemStack.getTagCompound().getLong(Constants.NBT.MOST_SIG), parentItemStack.getTagCompound().getLong(Constants.NBT.LEAST_SIG));
            for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
            {
                ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);

                if (hasUUID(itemStack))
                {
                    if (itemStack.getTagCompound().getLong(Constants.NBT.MOST_SIG) == parentItemStackUUID.getMostSignificantBits() && itemStack.getTagCompound().getLong(Constants.NBT.LEAST_SIG) == parentItemStackUUID.getLeastSignificantBits())
                    {
                        return itemStack;
                    }
                }
            }
        }

        return null;
    }

    public void save()
    {
        NBTTagCompound nbtTagCompound = parentItemStack.getTagCompound();

        if (nbtTagCompound == null)
        {
            nbtTagCompound = new NBTTagCompound();

            UUID uuid = UUID.randomUUID();
            nbtTagCompound.setLong(Constants.NBT.MOST_SIG, uuid.getMostSignificantBits());
            nbtTagCompound.setLong(Constants.NBT.LEAST_SIG, uuid.getLeastSignificantBits());
        }

        writeToNBT(nbtTagCompound);
        parentItemStack.setTagCompound(nbtTagCompound);
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return slotIndex >= 0 && slotIndex < this.inventory.length ? inventory[slotIndex] : null;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= decrementAmount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
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
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory[slotIndex] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();
        markDirty();
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
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public void markDirty()
    {
        // NOOP
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        // NOOP
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        // NOOP
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ISigil;
    }

    @Override
    public void clear()
    {
        this.inventory = new ItemStack[ItemSigilHolding.inventorySize];
    }

    @Override
    public String getName()
    {
        return "SigilOfHolding";
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

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        if (nbtTagCompound != null && nbtTagCompound.hasKey(Constants.NBT.ITEMS))
        {
            // Read in the ItemStacks in the inventory from NBT
            if (nbtTagCompound.hasKey(Constants.NBT.ITEMS))
            {
                NBTTagList tagList = nbtTagCompound.getTagList(Constants.NBT.ITEMS, 10);
                inventory = new ItemStack[this.getSizeInventory()];
                for (int i = 0; i < tagList.tagCount(); ++i)
                {
                    NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                    byte slotIndex = tagCompound.getByte("Slot");
                    if (slotIndex >= 0 && slotIndex < inventory.length)
                    {
                        inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
                    }
                }
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex)
        {
            if (inventory[currentIndex] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbtTagCompound.setTag(Constants.NBT.ITEMS, tagList);
    }

    public static boolean hasUUID(ItemStack itemStack)
    {
        return itemStack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && itemStack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG);
    }

    public static void setUUID(ItemStack itemStack)
    {
        itemStack = NBTHelper.checkNBT(itemStack);

        if (!itemStack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && !itemStack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG))
        {
            UUID itemUUID = UUID.randomUUID();
            itemStack.getTagCompound().setLong(Constants.NBT.MOST_SIG, itemUUID.getMostSignificantBits());
            itemStack.getTagCompound().setLong(Constants.NBT.LEAST_SIG, itemUUID.getLeastSignificantBits());
        }
    }
}
