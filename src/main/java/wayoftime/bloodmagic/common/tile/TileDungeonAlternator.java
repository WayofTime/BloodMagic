package wayoftime.bloodmagic.common.tile;

import static wayoftime.bloodmagic.common.block.BlockAlternator.ACTIVE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.base.TileTicking;

public class TileDungeonAlternator extends TileTicking
{

	private int cooldown = 0;

	public TileDungeonAlternator(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDungeonAlternator(BlockPos pos, BlockState state)
	{
		super(BloodMagicTileEntities.DUNGEON_ALTERNATOR_TYPE.get(), pos, state);
	}

	/**
	 * Called every tick that {@link #shouldTick()} is true.
	 */
	@Override
	public void onUpdate()
	{
		if (cooldown >= 40)
		{
			if (getBlockState().getValue(ACTIVE))
			{
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, false));

			} else
			{
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, true));
			}
			cooldown = 0;
		}
		cooldown++;
	}
}
