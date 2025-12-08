package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.FilterNodeTile;
import wayoftime.bloodmagic.common.blockentity.MasterNodeTile;

public class FilterNodeBlock extends RoutingNodeBlock {

    private final boolean isOutput;
    public FilterNodeBlock(boolean isOutput) {
        super();
        this.isOutput = isOutput;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(RENDER_LINE, true), UPDATE_ALL);
            level.scheduleTick(pos, state.getBlock(), 20 * 5);
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(state.getMenuProvider(level, pos), buf -> buf.writeBlockPos(pos).writeBoolean(isOutput));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity BE = level.getBlockEntity(pos);
        if (!(BE instanceof FilterNodeTile tile)) {
            return null;
        }
        return tile;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FilterNodeTile(pos, state, isOutput);
    }
}
