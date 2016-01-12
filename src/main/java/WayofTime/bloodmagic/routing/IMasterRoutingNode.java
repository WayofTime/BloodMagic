package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.util.BlockPos;

public interface IMasterRoutingNode extends IRoutingNode
{
    public boolean isConnected(List<BlockPos> path, BlockPos nodePos);

    public void addNodeToList(IRoutingNode node);

    public void addConnections(BlockPos pos, List<BlockPos> connectionList);

    public void addConnection(BlockPos pos1, BlockPos pos2);

    public void removeConnection(BlockPos pos1, BlockPos pos2);
}
