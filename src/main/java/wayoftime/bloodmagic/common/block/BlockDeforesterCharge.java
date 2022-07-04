package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileDeforesterCharge;

public class BlockDeforesterCharge extends BlockShapedExplosive
{
	public BlockDeforesterCharge(int explosionSize, Properties properties)
	{
		super(explosionSize, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileDeforesterCharge(explosionSize, pos, state);
	}
}
