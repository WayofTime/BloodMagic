package wayoftime.bloodmagic.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import wayoftime.bloodmagic.BloodMagic;

public abstract class AbstractGhostMenu<T extends AbstractContainerMenu> extends AbstractContainerMenu {

    public AbstractGhostMenu(MenuType<T> type, int containerId, Inventory playerInv, int dataSize, int rows, int columns, int xOff, int yOff, int invOff, int heldSlot) {
        this(type, containerId, playerInv, new SimpleContainerData(dataSize), new GhostItemHandler(rows * columns), rows, columns, xOff, yOff, invOff, heldSlot);
    }

    public AbstractGhostMenu(MenuType<T> type, int containerId, Inventory playerInventory, ContainerData tracker, GhostItemHandler handler, int rows, int columns, int xOff, int yOff, int invOff, int heldSlot) {
        super(type, containerId);
        this.tracker = tracker;
        this.handler = handler;
        this.addDataSlots(tracker);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.addSlot(new GhostSlot(handler, j + i * rows, xOff + j * 21, yOff + i * 21));
            }
        }

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, invOff + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            if (i == heldSlot) {
                this.addSlot(new DisabledSlot(playerInventory, i, 8 + i * 18, invOff + 58));
            } else {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, invOff + 58));
            }
        }
    }

    public final GhostItemHandler handler;
    public final ContainerData tracker;
    public int getLastGhostSlotClicked() {
        return tracker.get(0);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId < slots.size() && slotId >= 0) {
            Slot slot = slots.get(slotId);
            if (slot instanceof GhostSlot ghostSlot) {
                if ((button == 0 || button == 1)) {
                    ItemStack slotStack = slot.getItem();
                    ItemStack heldStack = this.getCarried();

                    if (button == 0) { // Left mouse click-eth
                        if (heldStack.isEmpty() && !slotStack.isEmpty()) {
                            // I clicked on the slot with an empty hand. Selecting!
                            updateGhostSelection(tracker.get(0), slotId);
                            BloodMagic.LOGGER.info("selected ghost slot {} index {}", slotId, slot.getSlotIndex());
                            tracker.set(0, slot.getSlotIndex());
                            // Return here to not save the server-side inventory
                            return;
                        } else if (!heldStack.isEmpty() && slotStack.isEmpty() && ghostSlot.isValid(heldStack)) {
                            ItemStack copyStack = heldStack.copy();
                            copyStack.setCount(1);
                            slot.set(copyStack);
                        }
                    } else { // Right mouse click-eth away
                        slot.set(ItemStack.EMPTY);
                    }
                }
            }
        }

        super.clicked(slotId, button, clickType, player);
    }

    // override for callback on ghost slot reselection
    public void updateGhostSelection(int previousSlot, int currentSlot) {}

    @Override // dont do quickMove here
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot movedSlot = this.slots.get(index);

        if (movedSlot.hasItem()) {
            ItemStack rawStack = movedSlot.getItem();
            movedStack = rawStack.copy();
        }

        return movedStack;
    }

    @Override
    public void setData(int id, int data) {
        super.setData(id, data);
        this.broadcastChanges();
    }

    public int getData(int id) {
        return this.tracker.get(id);
    }

    public static class GhostSlot extends SlotItemHandler {

        private final GhostItemHandler handler;
        public GhostSlot(GhostItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.handler = itemHandler;
        }

        public boolean isValid(ItemStack stack) {
            return handler.isItemValid(this.index, stack);
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return false;
        }

        @Override
        public void set(ItemStack stack) {
            super.set(stack);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public static class DisabledSlot extends Slot {

        public DisabledSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return false;
        }
    }
}
