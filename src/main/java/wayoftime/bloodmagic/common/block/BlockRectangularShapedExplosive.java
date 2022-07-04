package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileShapedExplosive;

public class BlockRectangularShapedExplosive extends BlockShapedExplosive
{
	public final int depth;

	public BlockRectangularShapedExplosive(int radius, int depth, Properties properties)
	{
		super(radius, properties);
		this.depth = depth;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileShapedExplosive(explosionSize, depth, pos, state);
	}
}
