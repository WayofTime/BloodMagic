package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ArcaneAshesTile extends BlockEntity {

    public ItemStackHandler inv = new ItemStackHandler(2);

    public ArcaneAshesTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.ARCANE_ASHES.get(), pos, blockState);
    }
}
