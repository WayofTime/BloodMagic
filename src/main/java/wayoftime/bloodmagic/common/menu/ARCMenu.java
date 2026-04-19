package wayoftime.bloodmagic.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.blockentity.ARCTile;

public class ARCMenu extends AbstractContainerMenu {

    public ARCTile tile;
    public ARCMenu(int containerId, Inventory playerInventory, ARCTile tile) {
        super(BMMenus.ARC.get(), containerId);

        this.tile = tile;
        this.addSlot(new SlotItemHandler(tile.getItemHandler(null), ARCTile.INPUT_BUCKET_SLOT, 8, 18));
        this.addSlot(new SlotItemHandler(tile.getItemHandler(null), ARCTile.OUTPUT_BUCKET_SLOT, 152, 90));
        this.addSlot(new SlotItemHandler(tile.getItemHandler(null), ARCTile.TOOL_SLOT, 35, 54));
        this.addSlot(new SlotItemHandler(tile.getItemHandler(null), ARCTile.INPUT_SLOT, 71, 18));

        for (int i = 0; i < ARCTile.NUM_OUTPUTS; i++) {
            this.addSlot(new SlotItemHandler(tile.getItemHandler(null), ARCTile.OUTPUT_SLOT + i, 116, 18 + i * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public void onTake(Player player, ItemStack stack) {
                    stack.onCraftedBy(player.level(), player, stack.getCount());
                    super.onTake(player, stack);
                }
            });
        }

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 184));
        }
    }

    public ARCMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, (ARCTile) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    private int playerInvStart = 9;
    private int playerInvEnd = playerInvStart + 27;
    private int hotbarStart = playerInvEnd + 1;
    private int hotbarEnd = hotbarStart + 8;
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot movedSlot = this.slots.get(index);

        if (movedSlot.hasItem()) {
            ItemStack rawStack = movedSlot.getItem();
            movedStack = rawStack.copy();

            if (index < playerInvStart) {
                if (!this.moveItemStackTo(rawStack, playerInvStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (index >= playerInvStart) {
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
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.ARC_BLOCK.block().get());
    }
}
