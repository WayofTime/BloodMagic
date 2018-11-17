package WayofTime.bloodmagic.ritual.crushing;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICrushingHandler {
    ItemStack getRecipeOutput(ItemStack input, World world, BlockPos pos);
    Integer getLpDrain();
    Double getWillDrain();
}
