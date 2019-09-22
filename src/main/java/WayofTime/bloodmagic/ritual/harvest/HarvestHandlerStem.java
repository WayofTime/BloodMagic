package WayofTime.bloodmagic.ritual.harvest;

import net.minecraft.block.Blocks;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

/**
 * Harvest handler for crops with stems such as Pumpkins and Melons. Rotation based crop blocks are a good reason
 * to use this (see pumpkins). <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStemCrop(BlockState, BlockState)}
 */
public class HarvestHandlerStem implements IHarvestHandler {

    public HarvestHandlerStem() {
        for (Direction facing : Direction.HORIZONTALS)
            HarvestRegistry.registerStemCrop(Blocks.PUMPKIN.getDefaultState().withProperty(PumpkinBlock.FACING, facing), Blocks.PUMPKIN_STEM.getDefaultState().withProperty(StemBlock.AGE, 7));

        HarvestRegistry.registerStemCrop(Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_STEM.getDefaultState().withProperty(StemBlock.AGE, 7));
    }

    @Override
    public boolean harvest(World world, BlockPos pos, BlockState state, List<ItemStack> drops) {
        Direction cropDir = state.getActualState(world, pos).getValue(StemBlock.FACING);

        if (cropDir != Direction.UP) {
            BlockPos cropPos = pos.offset(cropDir);
            BlockState probableCrop = world.getBlockState(cropPos);
            Collection<BlockState> registeredCrops = HarvestRegistry.getStemCrops().get(state);

            for (BlockState registeredCrop : registeredCrops) {
                if (registeredCrop == probableCrop) {
                    NonNullList<ItemStack> blockDrops = NonNullList.create();
                    probableCrop.getBlock().getDrops(blockDrops, world, cropPos, probableCrop, 0);
                    drops.addAll(blockDrops);
                    world.destroyBlock(cropPos, false);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean test(World world, BlockPos pos, BlockState state) {
        return HarvestRegistry.getStemCrops().containsKey(state);
    }
}
