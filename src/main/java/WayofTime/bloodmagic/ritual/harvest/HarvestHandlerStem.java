package WayofTime.bloodmagic.ritual.harvest;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

/**
 * Harvest handler for crops with stems such as Pumpkins and Melons. Rotation based crop blocks are a good reason
 * to use this (see pumpkins). <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStemCrop(IBlockState, IBlockState)}
 */
public class HarvestHandlerStem implements IHarvestHandler {

    public HarvestHandlerStem() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
            HarvestRegistry.registerStemCrop(Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, facing), Blocks.PUMPKIN_STEM.getDefaultState().withProperty(BlockStem.AGE, 7));

        HarvestRegistry.registerStemCrop(Blocks.MELON_BLOCK.getDefaultState(), Blocks.MELON_STEM.getDefaultState().withProperty(BlockStem.AGE, 7));
    }

    @Override
    public boolean harvest(World world, BlockPos pos, IBlockState state, List<ItemStack> drops) {
        EnumFacing cropDir = state.getActualState(world, pos).getValue(BlockStem.FACING);

        if (cropDir != EnumFacing.UP) {
            BlockPos cropPos = pos.offset(cropDir);
            IBlockState probableCrop = world.getBlockState(cropPos);
            Collection<IBlockState> registeredCrops = HarvestRegistry.getStemCrops().get(state);

            for (IBlockState registeredCrop : registeredCrops) {
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
    public boolean test(World world, BlockPos pos, IBlockState state) {
        return HarvestRegistry.getStemCrops().containsKey(state);
    }
}
