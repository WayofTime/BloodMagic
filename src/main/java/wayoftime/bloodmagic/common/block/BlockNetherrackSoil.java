package wayoftime.bloodmagic.common.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.MovingPistonBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class BlockNetherrackSoil extends Block
{
	public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE_0_7;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public BlockNetherrackSoil(AbstractBlock.Properties builder)
	{
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(MOISTURE, Integer.valueOf(0)));
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos))
		{
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock || blockstate.getBlock() instanceof MovingPistonBlock;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos())
				? Blocks.NETHERRACK.getDefaultState()
				: super.getStateForPlacement(context);
	}

	public boolean isTransparent(BlockState state)
	{
		return true;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		if (!state.isValidPosition(worldIn, pos))
		{
			turnToDirt(state, worldIn, pos);
		}

	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		int i = state.get(MOISTURE);
		if (!hasLifeEssence(worldIn, pos) && !worldIn.isRainingAt(pos.up()))
		{
			if (i > 0)
			{
				worldIn.setBlockState(pos, state.with(MOISTURE, Integer.valueOf(i - 1)), 2);
			} else if (!hasCrops(worldIn, pos))
			{
				turnToDirt(state, worldIn, pos);
			}
		} else if (i < 7)
		{
			worldIn.setBlockState(pos, state.with(MOISTURE, Integer.valueOf(7)), 2);
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

	public static void turnToDirt(BlockState state, World worldIn, BlockPos pos)
	{
		worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, Blocks.NETHERRACK.getDefaultState(), worldIn, pos));
	}

	private boolean hasCrops(IBlockReader worldIn, BlockPos pos)
	{
		BlockState plant = worldIn.getBlockState(pos.up());
		BlockState state = worldIn.getBlockState(pos);
		return plant.getBlock() instanceof net.minecraftforge.common.IPlantable && state.canSustainPlant(worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable) plant.getBlock());
	}

	private static boolean hasLifeEssence(IWorldReader worldIn, BlockPos pos)
	{
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
		{
			if (worldIn.getFluidState(blockpos).isTagged(BloodMagicTags.LIFE_ESSENCE))
			{
				return true;
			}
		}

		return false;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MOISTURE);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return false;
	}
}
