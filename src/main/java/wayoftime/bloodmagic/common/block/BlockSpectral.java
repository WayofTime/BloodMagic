package wayoftime.bloodmagic.common.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wayoftime.bloodmagic.common.block.type.SpectralBlockType;
import wayoftime.bloodmagic.common.tile.TileSpectral;

public class BlockSpectral extends Block implements EntityBlock
{
	public static final EnumProperty<SpectralBlockType> SPECTRAL_STATE = EnumProperty.create("spectral", SpectralBlockType.class);
	public static final int DECAY_RATE = 100;

	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 0, 0, 0);

	public BlockSpectral(Properties prop)
	{
		super(prop);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileSpectral)
			{
				((TileSpectral) tile).tick();
			}
		};
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext)
	{
		return true;
	}

	@Override
	public boolean canBeReplaced(BlockState state, Fluid fluid)
	{
		return false;
	}

	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileSpectral(pos, state);
	}


	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(SPECTRAL_STATE, SpectralBlockType.SOLID);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(SPECTRAL_STATE);
	}
}
