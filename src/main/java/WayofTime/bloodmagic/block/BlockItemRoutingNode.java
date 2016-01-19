package WayofTime.bloodmagic.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.routing.TileItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;

public class BlockItemRoutingNode extends BlockRoutingNode
{
    public BlockItemRoutingNode()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".itemRouting");
        setRegistryName(Constants.BloodMagicBlock.ITEM_ROUTING_NODE.getRegName());
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileItemRoutingNode();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRoutingNode)
        {
            ((TileRoutingNode) tile).removeAllConnections();
        }
        super.breakBlock(world, pos, state);
    }
}
