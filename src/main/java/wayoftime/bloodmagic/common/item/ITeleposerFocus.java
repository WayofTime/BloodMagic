package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import wayoftime.bloodmagic.common.datacomponent.Binding;

import java.util.List;

public interface ITeleposerFocus {
    AABB getEntityRangeOffset(Level world, BlockPos teleposerPos);

    List<BlockPos> getBlockListOffset(Level world);

    BlockPos getStoredPos(ItemStack stack);

    Level getStoredWorld(ItemStack stack, Level world);

    Binding getBinding(ItemStack stack);
}
