package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class IncenseAltarComponent {
    public final BlockPos offsetPos;
    public final Block block;
    public final IBlockState state;

    public IncenseAltarComponent(BlockPos offsetPos, Block block, IBlockState state) {
        this.offsetPos = offsetPos;
        this.block = block;
        this.state = state;
    }

    public boolean doesBlockMatch(Block block, IBlockState state) {
        return this.block == block && block.getMetaFromState(state) == this.block.getMetaFromState(this.state);
    }

    /**
     * Base rotation is north.
     */
    public BlockPos getOffset(EnumFacing rotation) {
        return new BlockPos(this.getX(rotation), offsetPos.getY(), this.getZ(rotation));
    }

    public int getX(EnumFacing direction) {
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

    public int getZ(EnumFacing direction) {
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
