package wayoftime.bloodmagic.common.block.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class WillRotatedPillarBlock extends RotatedPillarBlock {

    public WillRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, BlockWillType.WILL_TYPE);
    }
}
