package WayofTime.bloodmagic.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class TileInventory extends TileEntity implements IInventory
{
    protected int[] syncedSlots = new int[0];
    private ItemStack[] inventory;
    private int size;
    private String name;

    public TileInventory(int size, String name)
    {
        this.inventory = new ItemStack[size];
        this.size = size;
        this.name = name;
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

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
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
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
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

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(getPos(), -999, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
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
            if (!worldObj.isRemote)
                worldObj.markBlockForUpdate(this.pos);

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
        if (!worldObj.isRemote)
            worldObj.markBlockForUpdate(this.pos);
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
    public IChatComponent getDisplayName()
    {
        return new ChatComponentText(getName());
    }
}
