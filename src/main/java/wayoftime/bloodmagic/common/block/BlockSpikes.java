package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockSpikes extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape UP_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);


    public BlockSpikes(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

        switch (state.getValue(FACING)) {
            case UP:
                return UP_SHAPE;
            case DOWN:
                return DOWN_SHAPE;
            case NORTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return UP_SHAPE;
        }
    }

    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context){
        return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace());
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING);
    }


    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.getType() != EntityType.ITEM){
            entityIn.makeStuckInBlock(state, new Vector3d(0.55D, (double) 0.20F, 0.55D));
            entityIn.hurt(DamageSource.GENERIC, 2.0F);
        }
    }
    

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isAir()){
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }
}
