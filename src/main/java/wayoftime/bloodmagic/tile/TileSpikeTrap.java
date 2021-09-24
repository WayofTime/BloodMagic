package wayoftime.bloodmagic.tile;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
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
		if (world == null) return;
		if (!world.isRemote) {
			boolean flag = getBlockState().get(ACTIVE);
			BlockPos newPos = pos.offset(getBlockState().get(FACING));
			if (flag != world.isBlockPowered(pos)) {
				world.setBlockState(pos, getBlockState().func_235896_a_(ACTIVE), 2);
			}
			if(flag){
				if (world.getBlockState(newPos).isAir())
					world.setBlockState(newPos, BloodMagicBlocks.SPIKES.get().getDefaultState().with(FACING,getBlockState().get(FACING)));
			} else if (world.getBlockState(newPos).getBlock() == BloodMagicBlocks.SPIKES.get()) world.setBlockState(newPos, Blocks.AIR.getDefaultState());
		}
	}
}
