package wayoftime.bloodmagic.core.living;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public interface ILivingDyeable {
    default void onDye(ItemStack stack, DyeColor dyeColor){
        stack.getOrCreateTag().putString("armorColor", dyeColor.getName());
    }

    default String getDyeColor(ItemStack stack) {
        if (!stack.hasTag()) {
            return DyeColor.RED.getName();
        }
        String color = stack.getTag().getString("armorColor");
        return !color.isEmpty() ? color : DyeColor.RED.getName();
    }

}
