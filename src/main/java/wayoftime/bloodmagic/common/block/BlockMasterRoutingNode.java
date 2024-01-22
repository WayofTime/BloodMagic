package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.tile.routing.TileMasterRoutingNode;

public class BlockMasterRoutingNode extends BlockItemRoutingNode
{
	public BlockMasterRoutingNode()
	{
		super();
	}

//
//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (world.getTileEntity(pos) instanceof TileOutputRoutingNode) {
//            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
//        }
//
//        return true;
//    }

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, tile) -> {
			if (tile instanceof TileMasterRoutingNode)
			{
				((TileMasterRoutingNode) tile).tick();
			}
		};
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof TileMasterRoutingNode)
			{
				((TileMasterRoutingNode) tile).removeAllConnections();
				((TileMasterRoutingNode) tile).dropItems();
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileMasterRoutingNode(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileMasterRoutingNode))
			return InteractionResult.FAIL;

		NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return InteractionResult.SUCCESS;
	}
}
