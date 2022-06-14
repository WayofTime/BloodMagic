package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import wayoftime.bloodmagic.tile.TileDeforesterCharge;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDeforesterCharge extends BlockShapedExplosive
{
	public BlockDeforesterCharge(int explosionSize, Properties properties)
	{
		super(explosionSize, properties);
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileDeforesterCharge();
	}
}
