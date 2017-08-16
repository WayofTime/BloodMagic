package WayofTime.bloodmagic.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerMasterRoutingNode extends Container {
    private final IInventory tileMasterRoutingNode;

    public ContainerMasterRoutingNode(InventoryPlayer inventoryPlayer, IInventory tileMasterRoutingNode) {
        this.tileMasterRoutingNode = tileMasterRoutingNode;

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileMasterRoutingNode.isUsableByPlayer(playerIn);
    }
}
