package WayofTime.bloodmagic.routing;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IMasterRoutingNode extends IRoutingNode {
    boolean isConnected(List<BlockPos> path, BlockPos nodePos);

    void addNodeToList(IRoutingNode node);

    void addConnections(BlockPos pos, List<BlockPos> connectionList);

    void addConnection(BlockPos pos1, BlockPos pos2);

    void removeConnection(BlockPos pos1, BlockPos pos2);
}
