package wayoftime.bloodmagic.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
        addDataSlots(priorities);
        this.priorities = priorities;
        this.nodePos = nodePos;
        this.player = playerInv.player;
        this.isOutput = isOutput;

        // TODO extract into separate class so this is more sightly
        this.addSlot(new SlotItemHandler(filterInv, 0, 71, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.isEmpty())
                    return false;
                return getItemHandler().isItemValid(priorities.get(DATA_SIDE), stack);
            }

            @Override
            public ItemStack getItem() {
                return getItemHandler().getStackInSlot(priorities.get(DATA_SIDE));
            }

            @Override
            public void set(ItemStack stack) {
                ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(priorities.get(DATA_SIDE), stack);
                this.setChanged();
            }

            public void initialize(ItemStack stack) {
                ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(priorities.get(DATA_SIDE), stack);
                this.setChanged();
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return !this.getItemHandler().extractItem(priorities.get(DATA_SIDE), 1, true).isEmpty();
            }

            @Override
            public ItemStack remove(int amount) {
                return this.getItemHandler().extractItem(priorities.get(DATA_SIDE), amount, false);
            }
        });

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
                int side = priorities.get(DATA_SIDE);
                int prio = priorities.get(side);
                int newPrio = Math.clamp(prio + (id == BUTTON_PRIO_UP ? 1 : -1), 0, FilterNodeTile.MAX_PRIO);
                priorities.set(side, newPrio);
                broadcastChanges();
                yield true;
            }
            case BUTTON_EDIT -> {
                yield false;
            }
            case DATA_DOWN, DATA_UP, DATA_NORTH, DATA_SOUTH, DATA_WEST, DATA_EAST -> {
                priorities.set(DATA_SIDE, id);
                broadcastChanges();
                yield true;
            }
            default -> false;
        };
    }

    public int getData(int index) {
        return priorities.get(index);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // TODO at least implement shift-clicking filters out maybe
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(this.player.level(), this.nodePos), player, isOutput ?
                BMBlocks.OUTPUT_ROUTING_NODE.block().get() : BMBlocks.INPUT_ROUTING_NODE.block().get());
    }
}
