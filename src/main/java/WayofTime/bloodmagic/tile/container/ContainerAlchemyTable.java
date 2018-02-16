package WayofTime.bloodmagic.tile.container;

import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlchemyTable extends Container {
    private final IInventory tileTable;

    public ContainerAlchemyTable(InventoryPlayer inventoryPlayer, IInventory tileTable) {
        this.tileTable = tileTable;
        this.addSlotToContainer(new Slot(tileTable, 0, 62, 15));
        this.addSlotToContainer(new Slot(tileTable, 1, 80, 51));
        this.addSlotToContainer(new Slot(tileTable, 2, 62, 87));
        this.addSlotToContainer(new Slot(tileTable, 3, 26, 87));
        this.addSlotToContainer(new Slot(tileTable, 4, 8, 51));
        this.addSlotToContainer(new Slot(tileTable, 5, 26, 15));
        this.addSlotToContainer(new Slot(tileTable, TileAlchemyTable.toolSlot, 152, 33));
        this.addSlotToContainer(new SlotOrb(tileTable, TileAlchemyTable.orbSlot, 152, 69));
        this.addSlotToContainer(new SlotOutput(tileTable, TileAlchemyTable.outputSlot, 44, 51));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 181));
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        InventoryPlayer inventoryPlayer = player.inventory;

        if (slotId < 6 && slotId >= 0) {
            Slot slot = this.getSlot(slotId);
            if (!slot.getHasStack() && inventoryPlayer.getItemStack().isEmpty()) {
                ((TileAlchemyTable) tileTable).toggleInputSlotAccessible(slotId);
            }
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 8) {
                if (!this.mergeItemStack(itemstack1, 9, 9 + 36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 8) {
                if (itemstack1.getItem() instanceof IBloodOrb) {
                    if (!this.mergeItemStack(itemstack1, 7, 8, false)) //TODO: Add alchemy tools to list
                    {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, 6, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 9 + 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileTable.isUsableByPlayer(playerIn);
    }

    private class SlotOrb extends Slot {
        public SlotOrb(IInventory inventory, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return itemStack.getItem() instanceof IBloodOrb;
        }
    }

    private class SlotOutput extends Slot {
        public SlotOutput(IInventory inventory, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
