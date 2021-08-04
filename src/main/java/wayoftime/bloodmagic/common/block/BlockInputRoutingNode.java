package wayoftime.bloodmagic.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import wayoftime.bloodmagic.tile.routing.TileInputRoutingNode;

public class BlockInputRoutingNode extends BlockRoutingNode
{
	public BlockInputRoutingNode()
	{
		super();
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile instanceof TileInputRoutingNode)
		{
			((TileInputRoutingNode) tile).removeAllConnections();
			((TileInputRoutingNode) tile).dropItems();
		}
		super.onPlayerDestroy(world, blockPos, blockState);
	}

//
//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (world.getTileEntity(pos) instanceof TileInputRoutingNode) {
//            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
//        }
//
//        return true;
//    }
//
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileInputRoutingNode();
	}
}