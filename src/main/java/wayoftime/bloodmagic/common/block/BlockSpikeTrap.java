package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.tile.TileSpikeTrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockSpikeTrap extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockSpikeTrap(Properties properties) {
        super(properties);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(ACTIVE, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }
    
    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new TileSpikeTrap();
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE).add(FACING);
    }
}
