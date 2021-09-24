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
		if (cooldown >= 20) {
			if (getBlockState().get(ACTIVE)) {
				world.setBlockState(pos, getBlockState().with(ACTIVE, false));
				
			} else {
				world.setBlockState(pos, getBlockState().with(ACTIVE, true));
			}
			cooldown = 0;
		}
		cooldown++;
	}
}
