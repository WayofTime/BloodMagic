package WayofTime.bloodmagic.tile.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;

public class ContainerMasterRoutingNode extends Container {
    private final IInventory tileMasterRoutingNode;

    public ContainerMasterRoutingNode(PlayerInventory inventoryPlayer, IInventory tileMasterRoutingNode) {
        this.tileMasterRoutingNode = tileMasterRoutingNode;

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileMasterRoutingNode.isUsableByPlayer(playerIn);
    }
}
