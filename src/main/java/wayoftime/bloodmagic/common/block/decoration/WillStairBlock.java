package wayoftime.bloodmagic.common.block.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.function.Supplier;

public class WillStairBlock extends StairBlock {
    public WillStairBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56932_) {
        p_56932_.add(FACING, HALF, SHAPE, WATERLOGGED, BlockWillType.WILL_TYPE);
    }
}
