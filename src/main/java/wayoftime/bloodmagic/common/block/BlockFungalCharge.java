package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileFungalCharge;

public class BlockFungalCharge extends BlockShapedExplosive
{
	public BlockFungalCharge(int explosionSize, Properties properties)
	{
		super(explosionSize, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileFungalCharge(this.explosionSize, pos, state);
	}
}
