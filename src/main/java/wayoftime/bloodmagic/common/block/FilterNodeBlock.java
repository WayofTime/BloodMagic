package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.FilterNodeTile;

public class FilterNodeBlock extends RoutingNodeBlock {

    private final boolean isOutput;
    public FilterNodeBlock(boolean isOutput) {
        super();
        this.isOutput = isOutput;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FilterNodeTile(pos, state, isOutput);
    }
}
