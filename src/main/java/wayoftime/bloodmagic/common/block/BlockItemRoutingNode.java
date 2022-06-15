package wayoftime.bloodmagic.common.block;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileRoutingNode;

public class BlockItemRoutingNode extends BlockRoutingNode implements EntityBlock
{
	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		BlockEntity tile = world.getBlockEntity(blockPos);
		if (tile instanceof TileRoutingNode)
		{
			((TileRoutingNode) tile).removeAllConnections();
			((TileRoutingNode) tile).dropItems();
		}
		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tile = worldIn.getBlockEntity(pos);
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

					BlockEntity connectedTile = worldIn.getBlockEntity(connectedPos);
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileRoutingNode(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileRoutingNode)
			{
				((TileRoutingNode) tile).tick();
			}
		};
	}
}
