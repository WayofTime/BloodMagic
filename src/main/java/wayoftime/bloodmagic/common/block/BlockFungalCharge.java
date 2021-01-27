package wayoftime.bloodmagic.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wayoftime.bloodmagic.tile.TileFungalCharge;

public class BlockFungalCharge extends BlockShapedExplosive
{
	public BlockFungalCharge(int explosionSize, Properties properties)
	{
		super(explosionSize, properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileFungalCharge();
	}
}
