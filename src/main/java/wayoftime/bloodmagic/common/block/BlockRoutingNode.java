package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public class BlockRoutingNode extends Block
{
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public BlockRoutingNode()
	{
		super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2));

		this.setDefaultState(this.stateContainer.getBaseState().with(DOWN, false).with(UP, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
	}

//	@Override
//	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
//	{
//		return true;
//	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState returnState = super.getStateForPlacement(context);
		for (Direction dir : Direction.values())
		{
			BlockPos attachedPos = blockpos.offset(dir);
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
			returnState = returnState.with(prop, canConnect(attachedState, attachedState.isSolidSide(iblockreader, attachedPos, dir.getOpposite()), dir));
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
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		BlockState returnState = stateIn;
		for (Direction dir : Direction.values())
		{
			BlockPos attachedPos = currentPos.offset(dir);
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
			returnState = returnState.with(prop, canConnect(attachedState, attachedState.isSolidSide(worldIn, attachedPos, dir.getOpposite()), dir));
		}

		return returnState;
	}

	public boolean canConnect(BlockState state, boolean isSideSolid, Direction direction)
	{
//		      Block block = state.getBlock();
//		      boolean flag = this.isWoodenFence(block);
//		      boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(state, direction);
//		      return !cannotAttach(block) && isSideSolid || flag || flag1;

		return state.getMaterial().isOpaque() && isSideSolid;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}
}