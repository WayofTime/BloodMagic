package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class InventoryHolding implements IInventory
{
    public ItemStack parentItemStack;
    protected ItemStack[] inventory;

    protected static String NBT_MOST_SIG = "MostSig";
    protected static String NBT_LEAST_SIG = "LeastSig";
    protected static String NBT_ITEMS = "Items";

    public InventoryHolding(ItemStack itemStack)
    {
        parentItemStack = itemStack;

        inventory = new ItemStack[5];

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
            UUID parentItemStackUUID = new UUID(parentItemStack.getTagCompound().getLong(NBT_MOST_SIG), parentItemStack.getTagCompound().getLong(NBT_LEAST_SIG));
            for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
            {
                ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);

                if (hasUUID(itemStack))
                {
                    if (itemStack.getTagCompound().getLong(NBT_MOST_SIG) == parentItemStackUUID.getMostSignificantBits() && itemStack.getTagCompound().getLong(NBT_LEAST_SIG) == parentItemStackUUID.getLeastSignificantBits())
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
            nbtTagCompound.setLong(NBT_MOST_SIG, uuid.getMostSignificantBits());
            nbtTagCompound.setLong(NBT_LEAST_SIG, uuid.getLeastSignificantBits());
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
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        if (inventory[slotIndex] != null)
        {
            ItemStack itemStack = inventory[slotIndex];
            inventory[slotIndex] = null;
            return itemStack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        if (slotIndex >= 0 && slotIndex < this.inventory.length)
        {
            this.inventory[slotIndex] = itemStack;
        }
    }

    @Override
    public String getInventoryName()
    {
        return "SigilOfHolding";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
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
    public void openInventory()
    {
        // NOOP
    }

    @Override
    public void closeInventory()
    {
        // NOOP
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ISigil;
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        if (nbtTagCompound != null && nbtTagCompound.hasKey(NBT_ITEMS))
        {
            // Read in the ItemStacks in the inventory from NBT
            if (nbtTagCompound.hasKey(NBT_ITEMS))
            {
                NBTTagList tagList = nbtTagCompound.getTagList(NBT_ITEMS, 10);
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
        nbtTagCompound.setTag(NBT_ITEMS, tagList);
    }

    public static boolean hasTag(ItemStack itemStack, String keyName)
    {
        return itemStack != null && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey(keyName);
    }

    public static boolean hasUUID(ItemStack itemStack)
    {
        return hasTag(itemStack, NBT_MOST_SIG) && hasTag(itemStack, NBT_LEAST_SIG);
    }

    public static void setUUID(ItemStack itemStack)
    {
        initNBTTagCompound(itemStack);

        if (!hasTag(itemStack, NBT_MOST_SIG) && !hasTag(itemStack, NBT_LEAST_SIG))
        {
            UUID itemUUID = UUID.randomUUID();
            setLong(itemStack, NBT_MOST_SIG, itemUUID.getMostSignificantBits());
            setLong(itemStack, NBT_LEAST_SIG, itemUUID.getLeastSignificantBits());
        }
    }

    private static void initNBTTagCompound(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
    }

    public static void setLong(ItemStack itemStack, String keyName, long keyValue)
    {
        initNBTTagCompound(itemStack);

        itemStack.stackTagCompound.setLong(keyName, keyValue);
    }
}
