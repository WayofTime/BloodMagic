package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.BMTiles;
import wayoftime.bloodmagic.common.blockentity.MasterNodeTile;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;

public class MasterNodeBlock extends RoutingNodeBlock {
    public MasterNodeBlock() {
        super();
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return BlockEntityHelper.getTicker(blockEntityType, BMTiles.MASTER_ROUTING_NODE.get(), MasterNodeTile::tick);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MasterNodeTile(pos, state);
    }
}
