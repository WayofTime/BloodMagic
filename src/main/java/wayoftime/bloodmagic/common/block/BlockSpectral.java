package wayoftime.bloodmagic.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.common.block.type.SpectralBlockType;
import wayoftime.bloodmagic.tile.TileSpectral;

public class BlockSpectral extends Block
{
	public static final EnumProperty<SpectralBlockType> SPECTRAL_STATE = EnumProperty.create("spectral", SpectralBlockType.class);
	public static final int DECAY_RATE = 100;

	private static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 0, 0, 0);

	public BlockSpectral(Properties prop)
	{
		super(prop);
	}

	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		switch (state.get(SPECTRAL_STATE))
		{
		case SOLID:
			world.setBlockState(pos, state.with(SPECTRAL_STATE, SpectralBlockType.LEAKING), 3);
			world.getPendingBlockTicks().scheduleTick(pos, this, BlockSpectral.DECAY_RATE);
			break;
		case LEAKING:
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSpectral)
			{
				((TileSpectral) tile).revertToFluid();
			}
			break;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext)
	{
		return true;
	}

	@Override
	public boolean isReplaceable(BlockState state, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileSpectral();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(SPECTRAL_STATE, SpectralBlockType.SOLID);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(SPECTRAL_STATE);
	}
}
