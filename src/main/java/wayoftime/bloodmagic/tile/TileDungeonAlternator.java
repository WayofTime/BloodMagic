package wayoftime.bloodmagic.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.base.TileTicking;

import static wayoftime.bloodmagic.common.block.BlockAlternator.ACTIVE;

public class TileDungeonAlternator extends TileTicking {
	
	private int cooldown = 0;
	
	@ObjectHolder("bloodmagic:dungeonalternator")
	public static TileEntityType<TileDungeonAlternator> TYPE;
	
	public TileDungeonAlternator(TileEntityType<?> type) {
		super(type);
	}
	
	public TileDungeonAlternator() {
		super(TYPE);
	}
	
	/**
	 * Called every tick that {@link #shouldTick()} is true.
	 */
	@Override
	public void onUpdate() {
		if (cooldown >= 40) {
			if (getBlockState().getValue(ACTIVE)) {
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, false));
				
			} else {
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, true));
			}
			cooldown = 0;
		}
		cooldown++;
	}
}
