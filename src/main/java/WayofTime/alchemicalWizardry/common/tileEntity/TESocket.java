package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TESocket extends TEInventory
{
	public static final int sizeInv = 1;
	
    private int resultID;
    private int resultDamage;

    private boolean isActive;

    public TESocket()
    {
        super(sizeInv);
        resultID = 0;
        resultDamage = 0;
        isActive = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        resultID = par1NBTTagCompound.getInteger("resultID");
        resultDamage = par1NBTTagCompound.getInteger("resultDamage");
        isActive = par1NBTTagCompound.getBoolean("isActive");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("resultID", resultID);
        par1NBTTagCompound.setInteger("resultDamage", resultDamage);
        par1NBTTagCompound.setBoolean("isActive", isActive);
    }

    @Override
    public String getInventoryName()
    {
        return "TESocket";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    //Logic for the actual block is under here
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    public void setActive()
    {
        isActive = false;
    }

    public boolean isActive()
    {
        return isActive;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return NewPacketHandler.getPacket(this);
    }

    public void handlePacketData(int[] intData)
    {
        if (intData == null)
        {
            return;
        }

        if (intData.length == 3)
        {
            for (int i = 0; i < 1; i++)
            {
                if (intData[i * 3 + 2] != 0)
                {
                    ItemStack is = new ItemStack(Item.getItemById(intData[i * 3]), intData[i * 3 + 2], intData[i * 3 + 1]);
                    inv[i] = is;
                } else
                {
                    inv[i] = null;
                }
            }
        }
    }

    public int[] buildIntDataList()
    {
        int[] sortList = new int[1 * 3];
        int pos = 0;

        for (ItemStack is : inv)
        {
            if (is != null)
            {
                sortList[pos++] = Item.getIdFromItem(is.getItem());
                sortList[pos++] = is.getItemDamage();
                sortList[pos++] = is.stackSize;
            } else
            {
                sortList[pos++] = 0;
                sortList[pos++] = 0;
                sortList[pos++] = 0;
            }
        }

        return sortList;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == 0;
    }
}
