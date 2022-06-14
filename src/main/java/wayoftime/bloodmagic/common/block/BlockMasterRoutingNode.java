package wayoftime.bloodmagic.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.tile.routing.TileMasterRoutingNode;

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
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			TileEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof TileMasterRoutingNode)
			{
				((TileMasterRoutingNode) tile).removeAllConnections();
				((TileMasterRoutingNode) tile).dropItems();
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
		return new TileMasterRoutingNode();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (world.isClientSide)
			return ActionResultType.SUCCESS;

		TileEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileMasterRoutingNode))
			return ActionResultType.FAIL;

		NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return ActionResultType.SUCCESS;
	}
}
