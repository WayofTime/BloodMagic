package WayofTime.bloodmagic.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;

import javax.annotation.Nullable;

public class BlockMasterRoutingNode extends BlockRoutingNode
{
    public BlockMasterRoutingNode()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".masterRouting");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMasterRoutingNode();
    }

    //    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
//    {
//        if (world.getTileEntity(pos) instanceof TileMasterRoutingNode)
//        {
//            player.openGui(BloodMagic.instance, Constants.Gui.MASTER_ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
//        }
//
//        return true;
//    }
}
