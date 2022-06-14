package wayoftime.bloodmagic.common.block;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.tile.routing.TileRoutingNode;

public class BlockItemRoutingNode extends BlockRoutingNode
{
	@Override
	public void destroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileEntity tile = world.getBlockEntity(blockPos);
		if (tile instanceof TileRoutingNode)
		{
			((TileRoutingNode) tile).removeAllConnections();
			((TileRoutingNode) tile).dropItems();
		}
		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof TileRoutingNode)
			{
				List<BlockPos> connectionList = ((TileRoutingNode) tile).getConnected();
//				((TileRoutingNode) tile).removeAllConnections();
//				List<BlockPos> checkList = new LinkedList<BlockPos>();

				for (BlockPos connectedPos : connectionList)
				{
//					if (checkList.contains(connectedPos))
//					{
//						continue;
//					}

					TileEntity connectedTile = worldIn.getBlockEntity(connectedPos);
					if (connectedTile instanceof IRoutingNode)
					{
						List<BlockPos> checkResult = ((IRoutingNode) connectedTile).checkAndPurgeConnectionToMaster(pos);
//						checkList.addAll(checkResult);
					}
				}

				((TileRoutingNode) tile).removeAllConnections();
				((TileRoutingNode) tile).dropItems();

			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
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
