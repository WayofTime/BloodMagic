package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BlockSpikeTrap;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.base.TileTicking;

public class TileSpikeTrap extends TileTicking
{

//	@ObjectHolder("bloodmagic:spiketrap")
//	public static BlockEntityType<TileSpikeTrap> TYPE;

	public TileSpikeTrap(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileSpikeTrap(BlockPos pos, BlockState state)
	{
		super(BloodMagicTileEntities.SPIKE_TRAP_TYPE.get(), pos, state);
	}

	/**
	 * Called every tick that {@link #shouldTick()} is true.
	 */
	@Override
	public void onUpdate()
	{
		if (level == null)
			return;
		if (!level.isClientSide)
		{
			boolean flag = getBlockState().getValue(BlockSpikeTrap.ACTIVE);
			BlockPos newPos = worldPosition.relative(getBlockState().getValue(BlockStateProperties.FACING));
			if (flag != level.hasNeighborSignal(worldPosition))
			{
				level.setBlock(worldPosition, getBlockState().cycle(BlockSpikeTrap.ACTIVE), 2);
			}
			if (flag)
			{
				if (level.getBlockState(newPos).isAir())
				{
					level.setBlockAndUpdate(newPos, BloodMagicBlocks.SPIKES.get().defaultBlockState().setValue(BlockStateProperties.FACING, getBlockState().getValue(BlockStateProperties.FACING)));
					level.playSound(null, worldPosition, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.3f, 0.6f);
				}
			} else if (level.getBlockState(newPos).getBlock() == BloodMagicBlocks.SPIKES.get())
			{
				level.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
				level.playSound(null, worldPosition, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.3f, 0.6f);
			}
		}
	}
}
