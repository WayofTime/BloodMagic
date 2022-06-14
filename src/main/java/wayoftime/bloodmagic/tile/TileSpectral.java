package wayoftime.bloodmagic.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BlockSpectral;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.block.type.SpectralBlockType;
import wayoftime.bloodmagic.tile.base.TileBase;

public class TileSpectral extends TileBase
{
	@ObjectHolder("bloodmagic:spectral")
	public static TileEntityType<TileSpectral> TYPE;

	public BlockState storedBlock;

	public TileSpectral(TileEntityType<?> type)
	{
		super(type);
	}

	public TileSpectral()
	{
		this(TYPE);
	}

	public static void createOrRefreshSpectralBlock(World world, BlockPos pos)
	{
		if (world.isEmptyBlock(pos))
		{
			return;
		}

		BlockState potentialFluidBlockState = world.getBlockState(pos);

		if (isFluidBlock(potentialFluidBlockState.getBlock()))
		{
			world.setBlock(pos, BloodMagicBlocks.SPECTRAL.get().defaultBlockState(), 3);
			TileEntity spectralTile = world.getBlockEntity(pos);
			if (spectralTile instanceof TileSpectral)
			{
				((TileSpectral) spectralTile).setContainedBlockInfo(potentialFluidBlockState);
				world.getBlockTicks().scheduleTick(pos, spectralTile.getBlockState().getBlock(), BlockSpectral.DECAY_RATE);
			}
		} else if (potentialFluidBlockState.getBlock() == BloodMagicBlocks.SPECTRAL.get() && potentialFluidBlockState.getValue(BlockSpectral.SPECTRAL_STATE) == SpectralBlockType.LEAKING)
		{
			world.setBlock(pos, BloodMagicBlocks.SPECTRAL.get().defaultBlockState(), 0);
		}
	}

	public void revertToFluid()
	{
		level.setBlock(worldPosition, storedBlock, 3);
//		BlockState fluidState = Block.getStateById(meta);
	}

	public static boolean isFluidBlock(Block block)
	{
		return block instanceof FlowingFluidBlock;
	}

	public void setContainedBlockInfo(BlockState state)
	{
		storedBlock = state;

	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		storedBlock = NBTUtil.readBlockState(tag.getCompound("BlockState"));
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		tag.put("BlockState", NBTUtil.writeBlockState(storedBlock));
		return tag;
	}
}
