package WayofTime.bloodmagic.tile;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileMimic extends TileInventory
{
    public boolean dropItemsOnBreak = true;

    public TileMimic()
    {
        super(1, "mimic");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);

        return tag;
    }

    @Override
    public void dropItems()
    {
        if (dropItemsOnBreak)
        {
            InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == 0 && dropItemsOnBreak;
    }
}