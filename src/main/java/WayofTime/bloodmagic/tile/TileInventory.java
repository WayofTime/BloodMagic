package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.tile.base.TileBase;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileInventory extends TileBase implements IInventory {
    protected int[] syncedSlots = new int[0];
    protected NonNullList<ItemStack> inventory;
    IItemHandler handlerDown;
    IItemHandler handlerUp;
    IItemHandler handlerNorth;
    IItemHandler handlerSouth;
    IItemHandler handlerWest;
    IItemHandler handlerEast;
    private int size;

    // IInventory
    private String name;

    public TileInventory(int size, String name) {
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
        this.size = size;
        this.name = name;
        initializeItemHandlers();
    }

    protected boolean isSyncedSlot(int slot) {
        for (int s : this.syncedSlots) {
            if (s == slot) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        super.deserialize(tagCompound);
        NBTTagList tags = tagCompound.getTagList("Items", 10);
        inventory = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < tags.tagCount(); i++) {
            if (!isSyncedSlot(i)) {
                NBTTagCompound data = tags.getCompoundTagAt(i);
                byte j = data.getByte("Slot");

                if (j >= 0 && j < inventory.size()) {
                    inventory.set(j, new ItemStack(data)); // No matter how much an i looks like a j, it is not one. They are drastically different characters and cause drastically different things to happen. Apparently I didn't know this at one point. - TehNut
                }
            }
        }
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        super.serialize(tagCompound);
        NBTTagList tags = new NBTTagList();

        for (int i = 0; i < inventory.size(); i++) {
            if ((!inventory.get(i).isEmpty()) && !isSyncedSlot(i)) {
                NBTTagCompound data = new NBTTagCompound();
                data.setByte("Slot", (byte) i);
                inventory.get(i).writeToNBT(data);
                tags.appendTag(data);
            }
        }

        tagCompound.setTag("Items", tags);
        return tagCompound;
    }

    public void dropItems() {
        InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
    }

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (!getStackInSlot(index).isEmpty()) {
            if (!getWorld().isRemote)
                getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

            if (getStackInSlot(index).getCount() <= count) {
                ItemStack itemStack = inventory.get(index);
                inventory.set(index, ItemStack.EMPTY);
                markDirty();
                return itemStack;
            }

            ItemStack itemStack = inventory.get(index).splitStack(count);
            markDirty();
            return itemStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (!inventory.get(slot).isEmpty()) {
            ItemStack itemStack = inventory.get(slot);
            setInventorySlotContents(slot, ItemStack.EMPTY);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
        markDirty();
        if (!getWorld().isRemote)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    // IWorldNameable

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                return false;

        return true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public String getName() {
        return TextHelper.localize("tile.bloodmagic." + name + ".name");
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    protected void initializeItemHandlers() {
        if (this instanceof ISidedInventory) {
            handlerDown = new SidedInvWrapper((ISidedInventory) this, EnumFacing.DOWN);
            handlerUp = new SidedInvWrapper((ISidedInventory) this, EnumFacing.UP);
            handlerNorth = new SidedInvWrapper((ISidedInventory) this, EnumFacing.NORTH);
            handlerSouth = new SidedInvWrapper((ISidedInventory) this, EnumFacing.SOUTH);
            handlerWest = new SidedInvWrapper((ISidedInventory) this, EnumFacing.WEST);
            handlerEast = new SidedInvWrapper((ISidedInventory) this, EnumFacing.EAST);
        } else {
            handlerDown = new InvWrapper(this);
            handlerUp = handlerDown;
            handlerNorth = handlerDown;
            handlerSouth = handlerDown;
            handlerWest = handlerDown;
            handlerEast = handlerDown;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (facing) {
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
        } else if (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) handlerDown;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}
