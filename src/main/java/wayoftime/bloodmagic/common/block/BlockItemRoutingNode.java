package wayoftime.bloodmagic.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.routing.TileRoutingNode;

public class BlockItemRoutingNode extends BlockRoutingNode
{
	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile instanceof TileRoutingNode)
		{
			((TileRoutingNode) tile).removeAllConnections();
			((TileRoutingNode) tile).dropItems();
		}
		super.onPlayerDestroy(world, blockPos, blockState);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileRoutingNode)
			{
				((TileRoutingNode) tile).removeAllConnections();
				((TileRoutingNode) tile).dropItems();
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileRoutingNode();
	}
}
