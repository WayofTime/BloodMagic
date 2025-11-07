package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record NodeContext(BlockPos targetPos, Direction accessSide, FilterOperation op) {

    public static final NodeContext EMPTY = new NodeContext(BlockPos.ZERO, Direction.DOWN, FilterOperation.CAP_TEST);

    public enum FilterOperation {
        INPUT,
        OUTPUT,
        CAP_TEST
    }
}
