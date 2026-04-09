package wayoftime.bloodmagic.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
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
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.api.BMTags;

public class LivingStationMenu extends AbstractContainerMenu {

    public LivingStationMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(buf.readInt()));
    }

    // apparently these are evaluated before the constructor. I thought this only applied to static vars...
    private int playerInvStart;
    private int playerInvEnd;
    private int hotbarStart;
    private int hotbarEnd;
    public LivingStationMenu(int containerId, Inventory playerInventory, IItemHandler inv) {
        super(BMMenus.LIVING_STATION.get(), containerId);
        playerInvStart = inv.getSlots();
        playerInvEnd = playerInvStart + 27;
        hotbarStart = playerInvEnd + 1;
        hotbarEnd = hotbarStart + 8;

        // TODO whether or not something can go into a given slot is essentially duplicated between here and the actual inventory...
        this.addSlot(new SlotItemHandler(inv, 0, 12, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
                return tome != null && tome.upgrade().is(BMTags.Living.TOOLTIP_ORDER);
            }
        });
        this.addSlot(new SlotItemHandler(inv, 1, 84, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
                return (tome != null && tome.upgrade().is(BMTags.Living.IS_SCRAPPABLE))
                        || stack.is(BMItems.UPGRADE_SCRAP)
                        || stack.is(BMItems.SYNTHETIC_POINT);
            }
        });
        this.addSlot(new SlotItemHandler(inv, 2, 148, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        int rows = 5;
        int xOff = 8;
        int yOff = 45;
        int side = 18;
        setup:
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 9; j++) {
                if (3 + i * 9 + j >= inv.getSlots()) {
                    break setup;
                }
                this.addSlot(new SlotItemHandler(inv, 3 + i * 9 + j, xOff + j * side, yOff + i * side) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 149 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 207));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot movedSlot = this.slots.get(index);

        if (movedSlot.hasItem()) {
            ItemStack rawStack = movedSlot.getItem();
            movedStack = rawStack.copy();

            /* shift clicking on the tomes/scrap makes it fill your inventory with it. cant have that, sry
            if (index < playerInvStart) {
                if (!this.moveItemStackTo(rawStack, playerInvStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
             */

            if (index >= playerInvStart) {
                if (rawStack.is(BMItems.UPGRADE_TOME)) { // only insert tomes into store slot
                    if (!this.moveItemStackTo(rawStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (rawStack.is(BMItems.UPGRADE_SCRAP) || rawStack.is(BMItems.SYNTHETIC_POINT)) { // those go into the scrap slot
                    if (!this.moveItemStackTo(rawStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
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
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.LIVING_STATION.block().get());
    }
}
