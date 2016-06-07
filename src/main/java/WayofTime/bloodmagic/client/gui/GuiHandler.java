package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.item.inventory.ContainerHolding;
import WayofTime.bloodmagic.item.inventory.InventoryHolding;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.tile.container.ContainerAlchemyTable;
import WayofTime.bloodmagic.tile.container.ContainerItemRoutingNode;
import WayofTime.bloodmagic.tile.container.ContainerMasterRoutingNode;
import WayofTime.bloodmagic.tile.container.ContainerSoulForge;
import WayofTime.bloodmagic.tile.container.ContainerTeleposer;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);

        switch (id)
        {
        case Constants.Gui.TELEPOSER_GUI:
            return new ContainerTeleposer(player.inventory, (TileTeleposer) world.getTileEntity(pos));
        case Constants.Gui.SOUL_FORGE_GUI:
            return new ContainerSoulForge(player.inventory, (TileSoulForge) world.getTileEntity(pos));
        case Constants.Gui.ROUTING_NODE_GUI:
            return new ContainerItemRoutingNode(player.inventory, (TileFilteredRoutingNode) world.getTileEntity(pos));
        case Constants.Gui.MASTER_ROUTING_NODE_GUI:
            return new ContainerMasterRoutingNode(player.inventory, (TileMasterRoutingNode) world.getTileEntity(pos));
        case Constants.Gui.ALCHEMY_TABLE_GUI:
            return new ContainerAlchemyTable(player.inventory, (TileAlchemyTable) world.getTileEntity(pos));
        case Constants.Gui.SIGIL_HOLDING_GUI:
            return new ContainerHolding(player, new InventoryHolding(player.getHeldItemMainhand()));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        if (world instanceof WorldClient)
        {
            BlockPos pos = new BlockPos(x, y, z);

            switch (id)
            {
            case Constants.Gui.TELEPOSER_GUI:
                return new GuiTeleposer(player.inventory, (TileTeleposer) world.getTileEntity(pos));
            case Constants.Gui.SOUL_FORGE_GUI:
                return new GuiSoulForge(player.inventory, (TileSoulForge) world.getTileEntity(pos));
            case Constants.Gui.ROUTING_NODE_GUI:
                return new GuiItemRoutingNode(player.inventory, (TileFilteredRoutingNode) world.getTileEntity(pos));
            case Constants.Gui.MASTER_ROUTING_NODE_GUI:
                return new GuiMasterRoutingNode(player.inventory, (TileMasterRoutingNode) world.getTileEntity(pos));
            case Constants.Gui.ALCHEMY_TABLE_GUI:
                return new GuiAlchemyTable(player.inventory, (TileAlchemyTable) world.getTileEntity(pos));
            case Constants.Gui.SIGIL_HOLDING_GUI:
                return new GuiHolding(player, new InventoryHolding(player.getHeldItemMainhand()));
            }
        }

        return null;
    }
}
