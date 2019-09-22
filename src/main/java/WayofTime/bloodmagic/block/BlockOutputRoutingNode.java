package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.tile.routing.TileOutputRoutingNode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockOutputRoutingNode extends BlockRoutingNode {
    public BlockOutputRoutingNode() {
        super();

        setTranslationKey(BloodMagic.MODID + ".outputRouting");
    }

    @Override
    //TODO: Combine BlockOutputRoutingNode and BlockInputRoutingNode so they have the same superclass
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileOutputRoutingNode) {
            ((TileOutputRoutingNode) tile).removeAllConnections();
            ((TileOutputRoutingNode) tile).dropItems();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(pos) instanceof TileOutputRoutingNode) {
            player.openGui(BloodMagic.instance, Constants.Gui.ROUTING_NODE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileOutputRoutingNode();
    }
}
