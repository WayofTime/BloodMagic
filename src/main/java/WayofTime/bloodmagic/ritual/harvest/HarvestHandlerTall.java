package WayofTime.bloodmagic.ritual.harvest;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockReed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Harvest handler for crops that grow vertically such as Sugar Cane and Cactus. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerTallCrop(IBlockState)}
 */
public class HarvestHandlerTall implements IHarvestHandler {

    public HarvestHandlerTall() {
        for (int i = 0; i < 15; i++) {
            HarvestRegistry.registerTallCrop(Blocks.REEDS.getDefaultState().withProperty(BlockReed.AGE, i));
            HarvestRegistry.registerTallCrop(Blocks.CACTUS.getDefaultState().withProperty(BlockCactus.AGE, i));
        }
    }

    @Override
    public boolean harvest(World world, BlockPos pos, IBlockState state, List<ItemStack> drops) {
        IBlockState up = world.getBlockState(pos.up());
        if (up.getBlock() == state.getBlock()) {
            NonNullList<ItemStack> blockDrops = NonNullList.create();
            up.getBlock().getDrops(blockDrops, world, pos.up(), up, 0);
            drops.addAll(blockDrops);
            world.destroyBlock(pos.up(), false);
            return true;
        }

        return false;
    }

    @Override
    public boolean test(World world, BlockPos pos, IBlockState state) {
        return HarvestRegistry.getTallCrops().contains(state);
    }
}
