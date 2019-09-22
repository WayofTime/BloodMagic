package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockAlchemyTable extends BlockItem implements IVariantProvider {
    public ItemBlockAlchemyTable(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
        float yaw = player.rotationYaw;
        Direction direction = Direction.fromAngle(yaw);

        if (direction.getYOffset() != 0) {
            return false;
        }

        if (!world.isAirBlock(pos.offset(direction))) {
            return false;
        }

//        newState = block.getDefaultState().withProperty(BlockAlchemyTable.DIRECTION, direction).withProperty(BlockAlchemyTable.INVISIBLE, true);

        if (!world.setBlockState(pos, newState, 3)) {
            return false;
        }

        if (!world.setBlockState(pos.offset(direction), newState, 3)) {
            return false;
        }

        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileAlchemyTable) {
                ((TileAlchemyTable) tile).setInitialTableParameters(direction, false, pos.offset(direction));
            }

            TileEntity slaveTile = world.getTileEntity(pos.offset(direction));
            if (slaveTile instanceof TileAlchemyTable) {
                ((TileAlchemyTable) slaveTile).setInitialTableParameters(direction, true, pos);
            }

            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }

    @Override
    public void gatherVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "inventory");
    }
}