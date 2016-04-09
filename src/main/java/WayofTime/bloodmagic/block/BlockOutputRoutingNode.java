package WayofTime.bloodmagic.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;

public class BlockOutputRoutingNode extends BlockRoutingNode
{
    public BlockOutputRoutingNode()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".outputRouting");
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
        if (tile instanceof TileOutputRoutingNode)
        {
            ((TileOutputRoutingNode) tile).removeAllConnections();
            ((TileOutputRoutingNode) tile).dropItems();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.getTileEntity(pos) instanceof TileOutputRoutingNode)
        {
            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
