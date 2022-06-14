package wayoftime.bloodmagic.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileExplosiveCharge;
import wayoftime.bloodmagic.tile.TileShapedExplosive;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockShapedExplosive extends Block
{
	private static final VoxelShape UP = Block.box(2, 0, 2, 14, 7, 14);
	private static final VoxelShape DOWN = Block.box(2, 9, 2, 14, 16, 14);
	private static final VoxelShape NORTH = Block.box(2, 2, 7, 14, 14, 16);
	private static final VoxelShape SOUTH = Block.box(2, 2, 0, 14, 14, 7);
	private static final VoxelShape EAST = Block.box(0, 2, 2, 7, 14, 14);
	private static final VoxelShape WEST = Block.box(16, 2, 2, 9, 14, 14);

	public static final EnumProperty<Direction> ATTACHED = EnumProperty.create("attached", Direction.class);
	protected final int explosionSize;

	public BlockShapedExplosive(int explosionSize, Properties properties)
	{
		super(properties);
		this.explosionSize = explosionSize;

		this.registerDefaultState(this.stateDefinition.any().setValue(ATTACHED, Direction.UP));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return facing.getOpposite() == stateIn.getValue(ATTACHED) && !stateIn.canSurvive(worldIn, currentPos)
				? Blocks.AIR.defaultBlockState()
				: stateIn;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState blockstate = this.defaultBlockState();
		IWorldReader iworldreader = context.getLevel();
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ATTACHED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileShapedExplosive();
	}

	@Override
	public void playerWillDestroy(World world, BlockPos blockPos, BlockState blockState, PlayerEntity player)
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
