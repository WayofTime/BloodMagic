package wayoftime.bloodmagic.tile;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.tile.base.TileTicking;

import static net.minecraft.state.properties.BlockStateProperties.FACING;
import static wayoftime.bloodmagic.common.block.BlockSpikeTrap.ACTIVE;

public class TileSpikeTrap extends TileTicking {
	
	@ObjectHolder("bloodmagic:spiketrap")
	public static TileEntityType<TileSpikeTrap> TYPE;
	
	public TileSpikeTrap(TileEntityType<?> type) {
		super(type);
	}
	
	public TileSpikeTrap() {
		super(TYPE);
	}
	
	/**
	 * Called every tick that {@link #shouldTick()} is true.
	 */
	@Override
	public void onUpdate() {
		if (level == null) return;
		if (!level.isClientSide) {
			boolean flag = getBlockState().getValue(ACTIVE);
			BlockPos newPos = worldPosition.relative(getBlockState().getValue(FACING));
			if (flag != level.hasNeighborSignal(worldPosition)) {
				level.setBlock(worldPosition, getBlockState().cycle(ACTIVE), 2);
			}
			if(flag){
				if (level.getBlockState(newPos).isAir()) {
					level.setBlockAndUpdate(newPos, BloodMagicBlocks.SPIKES.get().defaultBlockState().setValue(FACING, getBlockState().getValue(FACING)));
					level.playSound(null, worldPosition, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.3f, 0.6f);
				}
			} else if (level.getBlockState(newPos).getBlock() == BloodMagicBlocks.SPIKES.get()){
				level.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
				level.playSound(null, worldPosition, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.3f, 0.6f);
			}
		}
	}
}
