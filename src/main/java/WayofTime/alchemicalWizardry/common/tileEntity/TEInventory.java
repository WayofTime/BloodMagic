package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

/**
 * Base class for tile entities with inventory
 * 
 * @author ljfa-ag
 */
public abstract class TEInventory extends TileEntity implements IInventory
{
    protected ItemStack[] inv;

    public TEInventory(int size)
    {
        inv = new ItemStack[size];
    }

    @Override
    public int getSizeInventory()
    {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inv[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.stackSize <= amt)
                setInventorySlotContents(slot, null);
            else
            {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0)
                    setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
            setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inv[slot] = stack;
        worldObj.markBlockForUpdate(pos);
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
    }

    @Override
    public abstract String getName();

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(this.pos) == this
                && player.getDistanceSqToCenter(pos) < 64;
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
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagList invList = new NBTTagList();
        for (int i = 0; i < inv.length; i++)
        {
            if (inv[i] != null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                inv[i].writeToNBT(stackTag);
                invList.appendTag(stackTag);
            }
        }

        tag.setTag("Inventory", invList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList invList = tag.getTagList("Inventory",
                Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < invList.tagCount(); i++)
        {
            NBTTagCompound stackTag = invList.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot");

            if (slot >= 0 && slot < inv.length)
                inv[slot] = ItemStack.loadItemStackFromNBT(stackTag);
        }
    }

    public void clear()
    {
        inv = new ItemStack[inv.length];
    }
    
    @Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
}
