package WayofTime.bloodmagic.client.gui;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.tile.container.ContainerItemRoutingNode;
import WayofTime.bloodmagic.tile.container.ContainerSoulForge;
import WayofTime.bloodmagic.tile.container.ContainerTeleposer;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;

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
            return new ContainerItemRoutingNode(player.inventory, (TileOutputRoutingNode) world.getTileEntity(pos));
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
                return new GuiItemRoutingNode(player.inventory, (TileOutputRoutingNode) world.getTileEntity(pos));
            }
        }

        return null;
    }
}
