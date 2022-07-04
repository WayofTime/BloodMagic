package wayoftime.bloodmagic.common.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.tile.TileExplosiveCharge;
import wayoftime.bloodmagic.common.tile.TileShapedExplosive;

public class BlockShapedExplosive extends Block implements EntityBlock
{
	private static final VoxelShape UP = Block.box(2, 0, 2, 14, 7, 14);
	private static final VoxelShape DOWN = Block.box(2, 9, 2, 14, 16, 14);
	private static final VoxelShape NORTH = Block.box(2, 2, 7, 14, 14, 16);
	private static final VoxelShape SOUTH = Block.box(2, 2, 0, 14, 14, 7);
	private static final VoxelShape EAST = Block.box(0, 2, 2, 7, 14, 14);
	private static final VoxelShape WEST = Block.box(9, 2, 2, 16, 14, 14);

	public static final EnumProperty<Direction> ATTACHED = EnumProperty.create("attached", Direction.class);
	protected final int explosionSize;

	public BlockShapedExplosive(int explosionSize, Properties properties)
	{
		super(properties);
		this.explosionSize = explosionSize;

		this.registerDefaultState(this.stateDefinition.any().setValue(ATTACHED, Direction.UP));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileShapedExplosive(explosionSize, explosionSize * 2 + 1, pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileExplosiveCharge)
			{
				((TileExplosiveCharge) tile).tick();
			}
		};
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return facing.getOpposite() == stateIn.getValue(ATTACHED) && !stateIn.canSurvive(worldIn, currentPos)
				? Blocks.AIR.defaultBlockState()
				: stateIn;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState blockstate = this.defaultBlockState();
		LevelReader iworldreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction[] adirection = context.getNearestLookingDirections();

		for (Direction direction : adirection)
		{
			Direction direction1 = direction.getOpposite();
			blockstate = blockstate.setValue(ATTACHED, direction1);
			if (blockstate.canSurvive(iworldreader, blockpos))
			{
				return blockstate;
			}
		}

		return null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(ATTACHED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		switch (state.getValue(ATTACHED))
		{
		case DOWN:
			return DOWN;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case EAST:
			return EAST;
		case WEST:
			return WEST;
		case UP:
		default:
			return UP;
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos blockPos, BlockState blockState, Player player)
	{
		TileExplosiveCharge tile = (TileExplosiveCharge) world.getBlockEntity(blockPos);
		if (tile != null && !world.isClientSide)
			tile.dropSelf();

		super.playerWillDestroy(world, blockPos, blockState, player);
	}

//	@Override
//	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//	{
//		if (!state.isIn(newState.getBlock()))
//		{
//			TileEntity tileentity = worldIn.getTileEntity(pos);
//			if (tileentity instanceof TileShapedExplosive)
//			{
//				((TileShapedExplosive) tileentity).dropSelf();
//			}
//
//			super.onReplaced(state, worldIn, pos, newState, isMoving);
//		}
//	}
}
