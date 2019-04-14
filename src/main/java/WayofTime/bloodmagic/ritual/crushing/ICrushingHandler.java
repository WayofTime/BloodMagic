package WayofTime.bloodmagic.ritual.crushing;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface ICrushingHandler {
    @Nonnull
    ItemStack getRecipeOutput(ItemStack input, World world, BlockPos pos);

    int getLpDrain();

    double getWillDrain();
}
