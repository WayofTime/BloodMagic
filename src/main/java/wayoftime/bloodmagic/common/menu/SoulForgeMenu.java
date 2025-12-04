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
import wayoftime.bloodmagic.common.blockentity.HellfireForgeTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public class SoulForgeMenu extends AbstractContainerMenu {

    public final HellfireForgeTile tile;

    public SoulForgeMenu(int containerId, Inventory playerInventory, HellfireForgeTile tile) {
        super(BMMenus.SOUL_FORGE.get(), containerId);
        this.tile = tile;

        // Input slots (4 corners)
        this.addSlot(new SlotItemHandler(tile.inv, 0, 8, 15));
        this.addSlot(new SlotItemHandler(tile.inv, 1, 80, 15));
        this.addSlot(new SlotItemHandler(tile.inv, 2, 8, 87));
        this.addSlot(new SlotItemHandler(tile.inv, 3, 80, 87));

        // Soul gem slot
        this.addSlot(new SlotItemHandler(tile.inv, HellfireForgeTile.GEM_SLOT, 152, 51) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.has(BMDataComponents.DEMON_WILL_AMOUNT);
            }
        });

        // Output slot
        this.addSlot(new SlotItemHandler(tile.inv, HellfireForgeTile.OUTPUT_SLOT, 44, 51) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 181));
        }
    }

    public SoulForgeMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, (HellfireForgeTile) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == HellfireForgeTile.OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, 6, 6 + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 5) {
                // From player inventory
                if (itemstack1.has(BMDataComponents.DEMON_WILL_AMOUNT)) {
                    // Will items go to soul slot
                    if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                    // Other items go to input slots
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 6, 6 + 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), player, BMBlocks.HELLFIRE_FORGE.block().get());
    }
}
