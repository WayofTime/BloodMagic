package WayofTime.bloodmagic.tile.routing;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.routing.IRoutingNode;

public class TileRoutingNode extends TileEntity implements IRoutingNode
{
    private BlockPos masterPos = BlockPos.ORIGIN;
    private List<BlockPos> connectionList = new LinkedList<BlockPos>();

    @Override
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
    {
        List<BlockPos> connectedList = this.getConnected();
        for (BlockPos testPos : connectedList)
        {
            if (alreadyChecked.contains(testPos))
            {
                continue;
            }
            alreadyChecked.add(testPos);
            TileEntity tile = world.getTileEntity(testPos);
            if (!(tile instanceof IRoutingNode))
            {
                continue;
            }
            IRoutingNode node = (IRoutingNode) tile;
            if (node.getMasterPos().equals(BlockPos.ORIGIN)) //If getMasterPos() returns the origin, the node is not connected to any master.
            {
                master.addNodeToList(node);
                node.connectMasterToRemainingNode(world, alreadyChecked, master);
            }
        }

        master.addConnections(this.getBlockPos(), connectedList);
    }

    @Override
    public BlockPos getBlockPos()
    {
        return this.getPos();
    }

    @Override
    public List<BlockPos> getConnected()
    {
        return connectionList;
    }

    @Override
    public BlockPos getMasterPos()
    {
        return masterPos;
    }

    @Override
    public boolean isConnectionEnabled(BlockPos testPos)
    {
        return true;
    }
}
