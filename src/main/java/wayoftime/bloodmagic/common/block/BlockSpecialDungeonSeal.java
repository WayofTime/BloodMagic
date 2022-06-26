package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileSpecialRoomDungeonSeal;

public class BlockSpecialDungeonSeal extends BlockDungeonSeal
{
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileSpecialRoomDungeonSeal(pos, state);
	}
}
