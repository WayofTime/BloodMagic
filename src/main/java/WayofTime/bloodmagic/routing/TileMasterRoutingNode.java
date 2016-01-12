package WayofTime.bloodmagic.routing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileMasterRoutingNode extends TileEntity implements IMasterRoutingNode
{
    // A list of connections
    private HashMap<BlockPos, List<BlockPos>> connectionMap = new HashMap<BlockPos, List<BlockPos>>();
    private List<BlockPos> generalNodeList = new LinkedList<BlockPos>();

    @Override
    public boolean isConnected(List<BlockPos> path, BlockPos nodePos)
    {
        if (!connectionMap.containsKey(nodePos))
        {
            return false;
        }
        TileEntity tile = worldObj.getTileEntity(nodePos);
        if (!(tile instanceof IRoutingNode))
        {
            connectionMap.remove(nodePos);
            return false;
        }

        IRoutingNode node = (IRoutingNode) tile;
        List<BlockPos> connectionList = node.getConnected();
        List<BlockPos> testPath = path.subList(0, path.size());
        testPath.add(nodePos);
        for (BlockPos testPos : connectionList)
        {
            if (testPath.contains(testPos))
            {
                continue;
            }

            if (testPos.equals(this.getPos()) && node.isConnectionEnabled(testPos))
            {
                path.clear();
                path.addAll(testPath);
                return true;
            } else if (NodeHelper.isNodeConnectionEnabled(worldObj, node, testPos))
            {
                if (isConnected(testPath, testPos))
                {
                    path.clear();
                    path.addAll(testPath);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isConnectionEnabled(BlockPos testPos)
    {
        return true;
    }

    @Override
    public void addNodeToList(IRoutingNode node)
    {
        BlockPos newPos = node.getBlockPos();
        if (!generalNodeList.contains(newPos))
        {
            generalNodeList.add(newPos);
        }

        //TODO: make more node lists so that you can more easily differentiate the different types of nodes
    }

    @Override
    public void addConnections(BlockPos pos, List<BlockPos> connectionList)
    {
        for (BlockPos testPos : connectionList)
        {
            addConnection(pos, testPos);
        }
    }

    @Override
    public void addConnection(BlockPos pos1, BlockPos pos2)
    {
        if (connectionMap.containsKey(pos1) && !connectionMap.get(pos1).contains(pos2))
        {
            connectionMap.get(pos1).add(pos2);
        } else
        {
            List<BlockPos> list = new LinkedList<BlockPos>();
            list.add(pos2);
            connectionMap.put(pos1, list);
        }

        if (connectionMap.containsKey(pos2) && !connectionMap.get(pos2).contains(pos1))
        {
            connectionMap.get(pos2).add(pos1);
        } else
        {
            List<BlockPos> list = new LinkedList<BlockPos>();
            list.add(pos1);
            connectionMap.put(pos2, list);
        }
    }

    @Override
    public void removeConnection(BlockPos pos1, BlockPos pos2)
    {
        if (connectionMap.containsKey(pos1))
        {
            List<BlockPos> posList = connectionMap.get(pos1);
            posList.remove(pos2);
            if (posList.isEmpty())
            {
                connectionMap.remove(pos1);
            }
        }

        if (connectionMap.containsKey(pos2))
        {
            List<BlockPos> posList = connectionMap.get(pos2);
            posList.remove(pos1);
            if (posList.isEmpty())
            {
                connectionMap.remove(pos2);
            }
        }
    }

    @Override
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
    {
        return;
    }

    @Override
    public BlockPos getBlockPos()
    {
        return this.getPos();
    }

    @Override
    public List<BlockPos> getConnected()
    {
        return new LinkedList<BlockPos>();
    }

    @Override
    public BlockPos getMasterPos()
    {
        return this.getPos();
    }
}
