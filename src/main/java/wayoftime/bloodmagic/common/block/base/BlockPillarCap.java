package wayoftime.bloodmagic.common.block.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockPillarCap extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public BlockPillarCap(Properties prop)
	{
		super(prop);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 * 
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
