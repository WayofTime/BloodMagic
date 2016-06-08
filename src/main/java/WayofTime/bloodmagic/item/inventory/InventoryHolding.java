package WayofTime.bloodmagic.item.inventory;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class InventoryHolding extends ItemInventory
{
    protected ItemStack[] inventory;

    public InventoryHolding(ItemStack itemStack)
    {
        super(itemStack, ItemSigilHolding.inventorySize, "SigilOfHolding");

//        readFromNBT(itemStack.getTagCompound());
    }

    public void onGuiSaved(EntityPlayer entityPlayer)
    {
        masterStack = findParentStack(entityPlayer);

        if (masterStack != null)
        {
            save();
        }
    }

    public ItemStack findParentStack(EntityPlayer entityPlayer)
    {
        if (hasUUID(masterStack))
        {
            UUID parentStackUUID = new UUID(masterStack.getTagCompound().getLong(Constants.NBT.MOST_SIG), masterStack.getTagCompound().getLong(Constants.NBT.LEAST_SIG));
            for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
            {
                ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);

                if (itemStack != null && hasUUID(itemStack))
                {
                    if (itemStack.getTagCompound().getLong(Constants.NBT.MOST_SIG) == parentStackUUID.getMostSignificantBits() && itemStack.getTagCompound().getLong(Constants.NBT.LEAST_SIG) == parentStackUUID.getLeastSignificantBits())
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
        NBTTagCompound nbtTagCompound = masterStack.getTagCompound();

        if (nbtTagCompound == null)
        {
            nbtTagCompound = new NBTTagCompound();

            UUID uuid = UUID.randomUUID();
            nbtTagCompound.setLong(Constants.NBT.MOST_SIG, uuid.getMostSignificantBits());
            nbtTagCompound.setLong(Constants.NBT.LEAST_SIG, uuid.getLeastSignificantBits());
        }

        writeToNBT(nbtTagCompound);
        masterStack.setTagCompound(nbtTagCompound);
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ISigil && !(itemStack.getItem() instanceof ItemSigilHolding);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    public static boolean hasUUID(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && itemStack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG);
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
