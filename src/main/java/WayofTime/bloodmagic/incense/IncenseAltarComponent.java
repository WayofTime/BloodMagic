package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class IncenseAltarComponent {
    public final BlockPos offsetPos;
    public final Block block;
    public final BlockState state;

    public IncenseAltarComponent(BlockPos offsetPos, Block block, BlockState state) {
        this.offsetPos = offsetPos;
        this.block = block;
        this.state = state;
    }

    public boolean doesBlockMatch(Block block, BlockState state) {
        return this.block == block && block.getMetaFromState(state) == this.block.getMetaFromState(this.state);
    }

    /**
     * Base rotation is north.
     */
    public BlockPos getOffset(Direction rotation) {
        return new BlockPos(this.getX(rotation), offsetPos.getY(), this.getZ(rotation));
    }

    public int getX(Direction direction) {
        switch (direction) {
            case EAST:
                return -this.offsetPos.getZ();
            case SOUTH:
                return -this.offsetPos.getX();
            case WEST:
                return this.offsetPos.getZ();
            default:
                return this.offsetPos.getX();
        }
    }

    public int getZ(Direction direction) {
        switch (direction) {
            case EAST:
                return this.offsetPos.getX();
            case SOUTH:
                return -this.offsetPos.getZ();
            case WEST:
                return -this.offsetPos.getX();
            default:
                return this.offsetPos.getZ();
        }
    }
}
