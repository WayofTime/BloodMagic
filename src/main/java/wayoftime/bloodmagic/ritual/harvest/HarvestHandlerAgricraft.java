package wayoftime.bloodmagic.ritual.harvest;

import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.util.helper.BlockProtectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class HarvestHandlerAgricraft implements IHarvestHandler {

    @Override
    public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops, @Nullable UUID ownerUUID) {
        if (world.getBlockEntity(pos) instanceof CropBlockEntity crop) {
            // Check protection before allowing harvest (Agricraft handles block modification internally)
            if (!BlockProtectionHelper.canPlaceBlock(world, pos, ownerUUID)) {
                return false;
            }
            return crop.harvest(drops::add, null);
        }

        return false;
    }

    @Override
    public boolean test(Level world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof CropBlockEntity crop) {
            return crop.canBeHarvested();
        }

        return false;
    }
}
