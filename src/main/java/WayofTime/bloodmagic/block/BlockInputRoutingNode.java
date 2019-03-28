package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.tile.routing.TileInputRoutingNode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockInputRoutingNode extends BlockRoutingNode {
    public BlockInputRoutingNode() {
        super();

        setTranslationKey(BloodMagic.MODID + ".inputRouting");
    }

    @Override
    //TODO: Combine BlockInputRoutingNode and BlockInputRoutingNode so they have the same superclass
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileInputRoutingNode) {
            ((TileInputRoutingNode) tile).removeAllConnections();
            ((TileInputRoutingNode) tile).dropItems();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(pos) instanceof TileInputRoutingNode) {
            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileInputRoutingNode();
    }
}
