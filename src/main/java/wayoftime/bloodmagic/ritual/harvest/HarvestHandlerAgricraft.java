package wayoftime.bloodmagic.ritual.harvest;

import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HarvestHandlerAgricraft implements IHarvestHandler {

    @Override
    public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops) {
        if (world.getBlockEntity(pos) instanceof CropBlockEntity crop) {
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
