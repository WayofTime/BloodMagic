package WayofTime.bloodmagic.ritual.harvest;

import net.minecraft.block.CactusBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Harvest handler for crops that grow vertically such as Sugar Cane and Cactus. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerTallCrop(BlockState)}
 */
public class HarvestHandlerTall implements IHarvestHandler {

    public HarvestHandlerTall() {
        for (int i = 0; i < 15; i++) {
            HarvestRegistry.registerTallCrop(Blocks.REEDS.getDefaultState().withProperty(SugarCaneBlock.AGE, i));
            HarvestRegistry.registerTallCrop(Blocks.CACTUS.getDefaultState().withProperty(CactusBlock.AGE, i));
        }
    }

    @Override
    public boolean harvest(World world, BlockPos pos, BlockState state, List<ItemStack> drops) {
        BlockState up = world.getBlockState(pos.up());
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
    public boolean test(World world, BlockPos pos, BlockState state) {
        return HarvestRegistry.getTallCrops().contains(state);
    }
}
