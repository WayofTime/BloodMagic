package wayoftime.bloodmagic.common.block;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import wayoftime.bloodmagic.common.block.type.PillarCapType;

public class BlockInversionPillarEnd extends Block
{
	public static final EnumProperty<PillarCapType> TYPE = EnumProperty.create("type", PillarCapType.class);
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public BlockInversionPillarEnd(BlockBehaviour.Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, PillarCapType.BOTTOM));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(TYPE);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context)
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

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
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
