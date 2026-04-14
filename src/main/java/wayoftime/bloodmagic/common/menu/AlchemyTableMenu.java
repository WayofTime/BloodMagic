package wayoftime.bloodmagic.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.blockentity.AlchemyTableTile;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;

import static wayoftime.bloodmagic.common.blockentity.AlchemyTableTile.*;

public class AlchemyTableMenu extends AbstractContainerMenu {

    public AlchemyTableMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInv, new ItemStackHandler(buf.readInt()), new SimpleContainerData(buf.readInt()));
    }

    public final ContainerData data;
    public AlchemyTableMenu(int containerId, Inventory playerInv, IItemHandler inv, ContainerData data) {
        super(BMMenus.ALCHEMY_TABLE.get(), containerId);
        this.data = data;
        addDataSlots(data);

        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 0, 26, 15) {
            @Override
            public int getMaxStackSize() {
                return super.getMaxStackSize();
            }
        });
        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 1, 62, 15));
        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 2, 8, 51));
        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 3, 80, 51));
        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 4, 26, 87));
        addSlot(new SlotItemHandler(inv, INPUT_SLOT + 5, 62, 87));

        addSlot(new SlotItemHandler(inv, ORB_SLOT, 143, 24) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItemHolder().getData(BMDataMaps.BLOOD_ORB_STATS) != null;
            }
        });
        addSlot(new SlotItemHandler(inv, OUTPUT_SLOT, 44, 51) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 181));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            setData(STACK_LIMIT, getData(STACK_LIMIT) == 0 ? 1 : 0);
            return true;
        }

        return false;
    }

    @Override
    public void setData(int id, int data) {
        super.setData(id, data);
        this.broadcastChanges();
    }

    public int getData(int id) {
        return this.data.get(id);
    }

    private int playerInvStart = SLOT_COUNT;
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
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.ALCHEMY_TABLE.block().get());
    }
}
