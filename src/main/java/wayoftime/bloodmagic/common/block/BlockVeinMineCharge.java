package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileVeinMineCharge;

public class BlockVeinMineCharge extends BlockShapedExplosive
{
	public BlockVeinMineCharge(int explosionSize, Properties properties)
	{
		super(explosionSize, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileVeinMineCharge(explosionSize, pos, state);
	}
}
