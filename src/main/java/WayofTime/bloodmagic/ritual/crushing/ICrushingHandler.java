package WayofTime.bloodmagic.ritual.crushing;

import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface ICrushingHandler {
    @Nonnull
    ItemStack getRecipeOutput(ItemStack input, World world, BlockPos pos);
    boolean hasResources(double will, int lp);
    int consumeResources(IMasterRitualStone mrs, EnumDemonWillType will, World world, BlockPos pos);
}
