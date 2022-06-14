package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.core.Direction;
import net.minecraft.util.Hand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.tile.TileDungeonAlternator;

import javax.annotation.Nullable;

public class BlockAlternator extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockAlternator(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        worldIn.getBlockTicks().scheduleTick(currentPos, this, 20);
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            worldIn.getBlockTicks().scheduleTick(pos, this, 1);

    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new TileDungeonAlternator();
    }
    
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public int getDirectSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }
    
    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
