package WayofTime.bloodmagic.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;

public class BlockMasterRoutingNode extends BlockRoutingNode
{
    public BlockMasterRoutingNode()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".masterRouting");
        setRegistryName(Constants.BloodMagicBlock.MASTER_ROUTING_NODE.getRegName());
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
        return new TileMasterRoutingNode();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.getTileEntity(pos) instanceof TileMasterRoutingNode)
        {
            player.openGui(BloodMagic.instance, Constants.Gui.MASTER_ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
