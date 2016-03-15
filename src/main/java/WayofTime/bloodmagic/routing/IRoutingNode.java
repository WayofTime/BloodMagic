package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRoutingNode
{
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
