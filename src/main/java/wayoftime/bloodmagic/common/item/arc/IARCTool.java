package wayoftime.bloodmagic.common.item.arc;

import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

public interface IARCTool {
    default double getCraftingSpeedMultiplier(ItemStack stack) {
        return 1;
    }

    default double getAdditionalOutputChanceMultiplier(ItemStack stack) {
        return 1;
    }

    default EnumWillType getDominantWillType(ItemStack stack) {
        return EnumWillType.DEFAULT;
    }
}
