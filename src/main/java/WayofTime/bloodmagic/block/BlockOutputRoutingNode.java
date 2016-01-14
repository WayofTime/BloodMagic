package WayofTime.bloodmagic.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.routing.IRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;

public class BlockOutputRoutingNode extends BlockContainer
{
    public BlockOutputRoutingNode()
    {
        super(Material.rock);

        setUnlocalizedName(Constants.Mod.MODID + ".outputRouting");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileOutputRoutingNode();
    }

    @Override
    //TODO: Combine BlockOutputRoutingNode and BlockInputRoutingNode so they have the same superclass
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IRoutingNode)
        {
            ((IRoutingNode) tile).removeAllConnections();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.getTileEntity(pos) instanceof TileOutputRoutingNode)
        {
            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;

//        if (world.isRemote)
//        {
//            return false;
//        }
//
//        TileEntity tile = world.getTileEntity(pos);
//        IRoutingNode node = (IRoutingNode) tile;
//        ChatUtil.sendChat(player, "Master: " + node.getMasterPos().toString());
//        for (BlockPos connPos : node.getConnected())
//        {
//            ChatUtil.sendChat(player, "Connected to: " + connPos.toString());
//        }
//
//        BlockPos masterPos = node.getMasterPos();
//        TileEntity testTile = world.getTileEntity(masterPos);
//        if (testTile instanceof IMasterRoutingNode)
//        {
//            IMasterRoutingNode master = (IMasterRoutingNode) testTile;
//            if (master.isConnected(new LinkedList<BlockPos>(), pos))
//            {
//                ChatUtil.sendChat(player, "Can find the path to the master");
//            }
//        }
//
//        return false;
    }
}
