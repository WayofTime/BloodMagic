package wayoftime.bloodmagic.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import wayoftime.bloodmagic.common.block.type.PillarCapType;

public class BlockInversionPillarEnd extends Block
{
	public static final EnumProperty<PillarCapType> TYPE = EnumProperty.create("type", PillarCapType.class);
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public BlockInversionPillarEnd(AbstractBlock.Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, PillarCapType.BOTTOM));
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(TYPE);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = context.getLevel().getBlockState(blockpos);
		if (!blockstate.is(this))
		{
			BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, PillarCapType.BOTTOM);
			Direction direction = context.getClickedFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D))
					? blockstate1
					: blockstate1.setValue(TYPE, PillarCapType.TOP);
		} else
		{
			return null;
		}
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		PillarCapType captype = state.getValue(TYPE);
		switch (captype)
		{
		case TOP:
			return TOP_SHAPE;
		default:
			return BOTTOM_SHAPE;
		}
	}
}
