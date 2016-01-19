package WayofTime.bloodmagic.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;

public class BlockInputRoutingNode extends BlockRoutingNode
{
    public BlockInputRoutingNode()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".inputRouting");
        setRegistryName(Constants.BloodMagicBlock.INPUT_ROUTING_NODE.getRegName());
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileInputRoutingNode();
    }

    @Override
    //TODO: Combine BlockInputRoutingNode and BlockInputRoutingNode so they have the same superclass
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileInputRoutingNode)
        {
            ((TileInputRoutingNode) tile).removeAllConnections();
            ((TileInputRoutingNode) tile).dropItems();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.getTileEntity(pos) instanceof TileInputRoutingNode)
        {
            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
