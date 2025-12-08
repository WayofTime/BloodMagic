package wayoftime.bloodmagic.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.blockentity.BMTiles;
import wayoftime.bloodmagic.common.blockentity.FilterNodeTile;
import wayoftime.bloodmagic.common.item.FilterItem;
import wayoftime.bloodmagic.common.tag.BMTags;

public class NodeFilterMenu extends AbstractContainerMenu {

    // I'd get the IDs directly from Direction but switch wants a "constant expression"
    public static final int DATA_DOWN = 0;
    public static final int DATA_UP = 1;
    public static final int DATA_NORTH = 2;
    public static final int DATA_SOUTH = 3;
    public static final int DATA_WEST = 4;
    public static final int DATA_EAST = 5;
    public static final int DATA_SIDE = 6;

    public static final int BUTTON_PRIO_DOWN = 6;
    public static final int BUTTON_PRIO_UP = 7;
    public static final int BUTTON_EDIT = 8;

    public NodeFilterMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInv, new ItemStackHandler(6), new SimpleContainerData(7), buf.readBlockPos(), buf.readBoolean());
    }

    private final ContainerData priorities;
    public final BlockPos nodePos;
    public final Player player;
    public final boolean isOutput;
    public NodeFilterMenu(int containerId, Inventory playerInv, IItemHandler filterInv, ContainerData priorities, BlockPos nodePos, boolean isOutput) {
        super(BMMenus.FILTERED_NODE.get(), containerId);
        this.priorities = priorities;
        addDataSlots(this.priorities);
        this.nodePos = nodePos;
        this.player = playerInv.player;
        this.isOutput = isOutput;

        this.addSlot(new FilterSlot(filterInv, 0, 71, 33));
        this.addSlot(new FilterSlot(filterInv, 1, 71, 33));
        this.addSlot(new FilterSlot(filterInv, 2, 71, 33));
        this.addSlot(new FilterSlot(filterInv, 3, 71, 33));
        this.addSlot(new FilterSlot(filterInv, 4, 71, 33));
        this.addSlot(new FilterSlot(filterInv, 5, 71, 33));

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 145));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return switch (id) {
            case BUTTON_PRIO_DOWN, BUTTON_PRIO_UP -> {
                int side = getData(DATA_SIDE);
                int prio = getData(side);
                int newPrio = Math.clamp(prio + (id == BUTTON_PRIO_UP ? 1 : -1), 0, FilterNodeTile.MAX_PRIO);
                setData(side, newPrio);
                yield true;
            }

            case BUTTON_EDIT -> {
                int slot = getData(DATA_SIDE);
                ItemStack filterStack = getSlot(slot).getItem();
                if (!filterStack.isEmpty() && filterStack.is(BMTags.Items.FILTERS)) {
                    player.openMenu(FilterItem.getFilterProvider(filterStack, -1, nodePos), buf -> FilterItem.writeBuf(filterStack, -1, nodePos, buf));
                }
                yield false;
            }

            case DATA_DOWN, DATA_UP, DATA_NORTH, DATA_SOUTH, DATA_WEST, DATA_EAST -> {
                setData(DATA_SIDE, id);
                yield true;
            }

            default -> false;
        };
    }

    public int getData(int index) {
        return priorities.get(index);
    }

    private static final int playerInvStart = 6;
    private static final int playerInvEnd = playerInvStart + (3 * 9); // end is exclusive anyways
    private static final int hotbarStart = playerInvEnd;
    private static final int hotbarEnd = hotbarStart + 9;
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
                int target = getData(DATA_SIDE);
                if (!this.moveItemStackTo(rawStack, target, target + 1, false)) {
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
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(this.player.level(), this.nodePos), player, isOutput ?
                BMBlocks.OUTPUT_ROUTING_NODE.block().get() : BMBlocks.INPUT_ROUTING_NODE.block().get());
    }

    public class FilterSlot extends SlotItemHandler {
        public FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isActive() {
            return index == NodeFilterMenu.this.getData(DATA_SIDE);
        }
    }
}
