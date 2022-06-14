package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.tile.TileDungeonAlternator;

import javax.annotation.Nullable;

public class BlockAlternator extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockAlternator(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        worldIn.getBlockTicks().scheduleTick(currentPos, this, 20);
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            worldIn.getBlockTicks().scheduleTick(pos, this, 1);

    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileDungeonAlternator();
    }
    
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public int getDirectSignal(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }
    
    @Override
    public int getSignal(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }
    
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
