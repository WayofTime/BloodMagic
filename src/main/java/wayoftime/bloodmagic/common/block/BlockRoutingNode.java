package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRoutingNode extends Block
{
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	protected static final VoxelShape SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public BlockRoutingNode()
	{
		super(BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops());
//.harvestTool(ToolType.PICKAXE).harvestLevel(2)

		this.registerDefaultState(this.stateDefinition.any().setValue(DOWN, false).setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
	}

//	@Override
//	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
//	{
//		return true;
//	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockGetter iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState returnState = super.getStateForPlacement(context);
		for (Direction dir : Direction.values())
		{
			BlockPos attachedPos = blockpos.relative(dir);
			BlockState attachedState = iblockreader.getBlockState(attachedPos);
			BooleanProperty prop = UP;
			switch (dir)
			{
			case DOWN:
				prop = DOWN;
				break;
			case EAST:
				prop = EAST;
				break;
			case NORTH:
				prop = NORTH;
				break;
			case SOUTH:
				prop = SOUTH;
				break;
			case UP:
				prop = UP;
				break;
			case WEST:
				prop = WEST;
				break;
			}
			returnState = returnState.setValue(prop, canConnect(attachedState, attachedState.isFaceSturdy(iblockreader, attachedPos, dir.getOpposite()), dir));
		}

		return returnState;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		BlockState returnState = stateIn;
		for (Direction dir : Direction.values())
		{
			BlockPos attachedPos = currentPos.relative(dir);
			BlockState attachedState = worldIn.getBlockState(attachedPos);
			BooleanProperty prop = UP;
			switch (dir)
			{
			case DOWN:
				prop = DOWN;
				break;
			case EAST:
				prop = EAST;
				break;
			case NORTH:
				prop = NORTH;
				break;
			case SOUTH:
				prop = SOUTH;
				break;
			case UP:
				prop = UP;
				break;
			case WEST:
				prop = WEST;
				break;
			}
			returnState = returnState.setValue(prop, canConnect(attachedState, attachedState.isFaceSturdy(worldIn, attachedPos, dir.getOpposite()), dir));
		}

		return returnState;
	}

	public boolean canConnect(BlockState state, boolean isSideSolid, Direction direction)
	{
//		      Block block = state.getBlock();
//		      boolean flag = this.isWoodenFence(block);
//		      boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(state, direction);
//		      return !cannotAttach(block) && isSideSolid || flag || flag1;

		return state.isSolid() && isSideSolid;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}
}