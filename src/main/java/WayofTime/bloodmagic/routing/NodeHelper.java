package WayofTime.bloodmagic.routing;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NodeHelper {
    public static boolean isNodeConnectionEnabled(World world, IRoutingNode node, BlockPos testPos) {
        if (!node.isConnectionEnabled(testPos)) {
            return false;
        }
        TileEntity tile = world.getTileEntity(testPos);
        if (!(tile instanceof IRoutingNode)) {
            return false;
        }

        IRoutingNode testNode = (IRoutingNode) tile;

        return testNode.isConnectionEnabled(node.getBlockPos());
    }
}
