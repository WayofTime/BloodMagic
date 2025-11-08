package wayoftime.bloodmagic.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import wayoftime.bloodmagic.common.block.BMBlocks;

public class NodeFilterMenu extends AbstractContainerMenu {
    public NodeFilterMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(6), new SimpleContainerData(6));
    }

    public NodeFilterMenu(int containerId, Inventory playerInv, IItemHandler filterInv, ContainerData priorities) {
        super(BMMenus.FILTERED_NODE.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // TODO at least implement shift-clicking filters out maybe
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        // TODO add buffer to menu creation and send isOutput so this can be checked properly
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.INPUT_ROUTING_NODE.block().get())
                || AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, BMBlocks.OUTPUT_ROUTING_NODE.block().get());
    }
}
