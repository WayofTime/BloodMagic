package wayoftime.bloodmagic.common.routing;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRoutingNode
{
	void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master);

	BlockPos getCurrentBlockPos();

	List<BlockPos> getConnected();

	BlockPos getMasterPos();

	boolean isConnectionEnabled(BlockPos testPos);

	boolean isMaster(IMasterRoutingNode master);

	void addConnection(BlockPos pos1);

	void removeConnection(BlockPos pos1);

	void removeAllConnections();

	// Returns the checked node locations.
	List<BlockPos> checkAndPurgeConnectionToMaster(BlockPos ignorePos);

	Triple<Boolean, List<BlockPos>, List<IRoutingNode>> recheckConnectionToMaster(List<BlockPos> alreadyChecked, List<IRoutingNode> nodeList);
}
