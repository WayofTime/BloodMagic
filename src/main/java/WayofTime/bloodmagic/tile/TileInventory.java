package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.tile.base.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class TileInventory extends TileBase implements IInventory
{
    protected int[] syncedSlots = new int[0];
    protected ItemStack[] inventory;
    private int size;
    private String name;

    public TileInventory(int size, String name)
    {
        this.inventory = new ItemStack[size];
        this.size = size;
        this.name = name;
        initializeItemHandlers();
    }

    protected boolean isSyncedSlot(int slot)
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

    @Override
    public void deserialize(NBTTagCompound tagCompound)
    {
        super.deserialize(tagCompound);
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

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound)
    {
        super.serialize(tagCompound);
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
        return tagCompound;
    }

    public void dropItems()
    {
        InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
    }

    // IInventory

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
            if (!getWorld().isRemote)
                getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

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
        if (!getWorld().isRemote)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
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

    // IWorldNameable

    @Override
    public String getName()
    {
        return TextHelper.localize("tile.BloodMagic." + name + ".name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(getName());
    }

    protected void initializeItemHandlers()
    {
        if (this instanceof ISidedInventory)
        {
            handlerDown = new SidedInvWrapper((ISidedInventory) this, EnumFacing.DOWN);
            handlerUp = new SidedInvWrapper((ISidedInventory) this, EnumFacing.UP);
            handlerNorth = new SidedInvWrapper((ISidedInventory) this, EnumFacing.NORTH);
            handlerSouth = new SidedInvWrapper((ISidedInventory) this, EnumFacing.SOUTH);
            handlerWest = new SidedInvWrapper((ISidedInventory) this, EnumFacing.WEST);
            handlerEast = new SidedInvWrapper((ISidedInventory) this, EnumFacing.EAST);
        } else
        {
            handlerDown = new InvWrapper(this);
            handlerUp = handlerDown;
            handlerNorth = handlerDown;
            handlerSouth = handlerDown;
            handlerWest = handlerDown;
            handlerEast = handlerDown;
        }
    }

    IItemHandler handlerDown;
    IItemHandler handlerUp;
    IItemHandler handlerNorth;
    IItemHandler handlerSouth;
    IItemHandler handlerWest;
    IItemHandler handlerEast;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            switch (facing)
            {
            case DOWN:
                return (T) handlerDown;
            case EAST:
                return (T) handlerEast;
            case NORTH:
                return (T) handlerNorth;
            case SOUTH:
                return (T) handlerSouth;
            case UP:
                return (T) handlerUp;
            case WEST:
                return (T) handlerWest;
            }
        } else if (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) handlerDown;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}
