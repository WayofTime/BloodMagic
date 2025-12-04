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
import wayoftime.bloodmagic.common.blockentity.AlchemyTableTile;
import wayoftime.bloodmagic.common.item.BloodOrbItem;

public class AlchemyTableMenu extends AbstractContainerMenu {

    public final AlchemyTableTile tile;

    public AlchemyTableMenu(int containerId, Inventory playerInventory, AlchemyTableTile tile) {
        super(BMMenus.ALCHEMY_TABLE.get(), containerId);
        this.tile = tile;

        this.addSlot(new SlotItemHandler(tile.inv, 0, 62, 15));
        this.addSlot(new SlotItemHandler(tile.inv, 1, 80, 51));
        this.addSlot(new SlotItemHandler(tile.inv, 2, 62, 87));
        this.addSlot(new SlotItemHandler(tile.inv, 3, 26, 87));
        this.addSlot(new SlotItemHandler(tile.inv, 4, 8, 51));
        this.addSlot(new SlotItemHandler(tile.inv, 5, 26, 15));
        this.addSlot(new SlotItemHandler(tile.inv, AlchemyTableTile.ORB_SLOT, 143, 24) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BloodOrbItem;
            }
        });
        this.addSlot(new SlotItemHandler(tile.inv, AlchemyTableTile.OUTPUT_SLOT, 44, 51) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 181));
        }
    }

    public AlchemyTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, (AlchemyTableTile) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == AlchemyTableTile.OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, 8, 8 + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 7) {
                if (itemstack1.getItem() instanceof BloodOrbItem) {
                    if (!this.moveItemStackTo(itemstack1, 6, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 8, 8 + 36, false)) {
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
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), player, BMBlocks.ALCHEMY_TABLE.block().get());
    }
}
