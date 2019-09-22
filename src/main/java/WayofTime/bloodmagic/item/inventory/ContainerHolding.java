package WayofTime.bloodmagic.item.inventory;

import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ContainerHolding extends Container {
    public final InventoryHolding inventoryHolding;
    private final int PLAYER_INVENTORY_ROWS = 3;
    private final int PLAYER_INVENTORY_COLUMNS = 9;
    private final PlayerEntity player;

    public ContainerHolding(PlayerEntity player, InventoryHolding inventoryHolding) {
        this.player = player;
        this.inventoryHolding = inventoryHolding;
        int currentSlotHeldIn = player.inventory.currentItem;

        for (int columnIndex = 0; columnIndex < ItemSigilHolding.inventorySize; ++columnIndex) {
            this.addSlotToContainer(new SlotHolding(this, inventoryHolding, player, columnIndex, 8 + columnIndex * 36, 17));
        }

        for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex) {
            for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex) {
                this.addSlotToContainer(new Slot(player.inventory, columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 41 + rowIndex * 18));
            }
        }

        for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex) {
            if (actionBarIndex == currentSlotHeldIn) {
                this.addSlotToContainer(new SlotDisabled(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 99));
            } else {
                this.addSlotToContainer(new Slot(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 99));
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity entityPlayer) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityPlayer) {
        super.onContainerClosed(entityPlayer);

        if (!entityPlayer.getEntityWorld().isRemote) {
            saveInventory(entityPlayer);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (!player.getEntityWorld().isRemote) {
            saveInventory(player);
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity entityPlayer, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = inventorySlots.get(slotIndex);
        int slots = inventorySlots.size();

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if (stack.getItem() instanceof ISigil) {
                if (slotIndex < ItemSigilHolding.inventorySize) {
                    if (!this.mergeItemStack(stackInSlot, ItemSigilHolding.inventorySize, slots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(stackInSlot, 0, ItemSigilHolding.inventorySize, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.getItem() instanceof ItemSigilHolding) {
                if (slotIndex < ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS)) {
                    if (!this.mergeItemStack(stackInSlot, ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(stackInSlot, ItemSigilHolding.inventorySize, ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slotObject.putStack(ItemStack.EMPTY);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slotObject.onTake(player, stackInSlot);
        }

        return stack;
    }

    public void saveInventory(PlayerEntity entityPlayer) {
        inventoryHolding.onGuiSaved(entityPlayer);
    }

    private class SlotHolding extends Slot {
        private final PlayerEntity player;
        private ContainerHolding containerHolding;

        public SlotHolding(ContainerHolding containerHolding, IInventory inventory, PlayerEntity player, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
            this.player = player;
            this.containerHolding = containerHolding;
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();

            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                containerHolding.saveInventory(player);
            }
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return itemStack.getItem() instanceof ISigil && !(itemStack.getItem() instanceof ItemSigilHolding);
        }
    }

    private class SlotDisabled extends Slot {
        public SlotDisabled(IInventory inventory, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return false;
        }

        @Override
        public boolean canTakeStack(PlayerEntity player) {
            return false;
        }
    }
}
