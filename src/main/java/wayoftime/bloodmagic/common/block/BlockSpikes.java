package wayoftime.bloodmagic.common.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSpikes extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	protected static final VoxelShape UP_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D);
	protected static final VoxelShape WEST_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);

	public BlockSpikes(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{

		switch (state.getValue(FACING))
		{
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
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
	{
		if (entityIn.getType() != EntityType.ITEM)
		{
			entityIn.makeStuckInBlock(state, new Vec3(0.55D, (double) 0.20F, 0.55D));
			entityIn.hurt(entityIn.damageSources().generic(), 2.0F);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		if (worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isAir())
		{
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}
}
