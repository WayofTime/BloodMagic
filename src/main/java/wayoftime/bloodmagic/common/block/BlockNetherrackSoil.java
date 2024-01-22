package wayoftime.bloodmagic.common.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class BlockNetherrackSoil extends Block
{
	public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public BlockNetherrackSoil(BlockBehaviour.Properties builder)
	{
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, Integer.valueOf(0)));
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos))
		{
			worldIn.scheduleTick(currentPos, this, 1);
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos)
	{
		BlockState blockstate = worldIn.getBlockState(pos.above());
		return !blockstate.isSolid() || blockstate.getBlock() instanceof FenceGateBlock || blockstate.getBlock() instanceof MovingPistonBlock;
	}

	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())
				? Blocks.NETHERRACK.defaultBlockState()
				: super.getStateForPlacement(context);
	}

	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return true;
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand)
	{
		if (!state.canSurvive(worldIn, pos))
		{
			turnToDirt(state, worldIn, pos);
		}

	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
	{
		int i = state.getValue(MOISTURE);
		if (!hasLifeEssence(worldIn, pos) && !worldIn.isRainingAt(pos.above()))
		{
			if (i > 0)
			{
				worldIn.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(i - 1)), 2);
			} else if (!hasCrops(worldIn, pos))
			{
				turnToDirt(state, worldIn, pos);
			}
		} else if (i < 7)
		{
			worldIn.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(7)), 2);
		}

	}

//	/**
//	 * Block's chance to react to a living entity falling on it.
//	 */
//	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
//	{
//		if (!worldIn.isRemote && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(worldIn, pos, Blocks.NETHERRACK.getDefaultState(), fallDistance, entityIn))
//		{ // Forge: Move logic to Entity#canTrample
//			turnToDirt(worldIn.getBlockState(pos), worldIn, pos);
//		}
//
//		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
//	}

	public static void turnToDirt(BlockState state, Level worldIn, BlockPos pos)
	{
		worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.NETHERRACK.defaultBlockState(), worldIn, pos));
	}

	private boolean hasCrops(BlockGetter worldIn, BlockPos pos)
	{
		BlockState plant = worldIn.getBlockState(pos.above());
		BlockState state = worldIn.getBlockState(pos);
		return plant.getBlock() instanceof net.minecraftforge.common.IPlantable && state.canSustainPlant(worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable) plant.getBlock());
	}

	private static boolean hasLifeEssence(LevelReader worldIn, BlockPos pos)
	{
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4)))
		{
			if (worldIn.getFluidState(blockpos).is(BloodMagicTags.LIFE_ESSENCE))
			{
				return true;
			}
		}

		return false;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MOISTURE);
	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type)
	{
		return false;
	}
}
