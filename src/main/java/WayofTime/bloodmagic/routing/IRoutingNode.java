package WayofTime.bloodmagic.routing;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IRoutingNode {
    void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master);

    BlockPos getBlockPos();

    List<BlockPos> getConnected();

    BlockPos getMasterPos();

    boolean isConnectionEnabled(BlockPos testPos);

    boolean isMaster(IMasterRoutingNode master);

    void addConnection(BlockPos pos1);

    void removeConnection(BlockPos pos1);

    void removeAllConnections();
}
