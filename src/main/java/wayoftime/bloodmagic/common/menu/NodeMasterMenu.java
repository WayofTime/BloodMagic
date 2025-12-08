package wayoftime.bloodmagic.common.menu;


import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BMBlocks;

public class NodeMasterMenu extends AbstractContainerMenu {

    public NodeMasterMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(2));
    }

    private int playerInvStart;
    private int playerInvEnd;
    private int hotbarStart;
    private int hotbarEnd;
    public NodeMasterMenu(int containerId, Inventory playerInv, IItemHandler inv) {
        super(BMMenus.MASTER_NODE.get(), containerId);
        playerInvStart = inv.getSlots();
        playerInvEnd = playerInvStart + 27;
        hotbarStart = playerInvEnd + 1;
        hotbarEnd = hotbarStart + 9;

        this.addSlot(new SlotItemHandler(inv, 0, 62, 20));
        this.addSlot(new SlotItemHandler(inv, 1, 98, 20));

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 44 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 102));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot movedSlot = this.slots.get(index);

        if (movedSlot.hasItem()) {
            ItemStack rawStack = movedSlot.getItem();
            movedStack = rawStack.copy();

            if (index <= playerInvStart) {
                if (!this.moveItemStackTo(rawStack, playerInvStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (index > playerInvStart) {
                if (!this.moveItemStackTo(rawStack, 0, playerInvStart, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (rawStack.isEmpty()) {
                movedSlot.set(ItemStack.EMPTY);
            } else {
                movedSlot.setChanged();
            }
        }

        return movedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.MASTER_NODE.block().get());
    }
}
