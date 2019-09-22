package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.tile.routing.TileItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockItemRoutingNode extends BlockRoutingNode {
    public BlockItemRoutingNode() {
        super();

        setTranslationKey(BloodMagic.MODID + ".itemRouting");
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRoutingNode) {
            ((TileRoutingNode) tile).removeAllConnections();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileItemRoutingNode();
    }
}
