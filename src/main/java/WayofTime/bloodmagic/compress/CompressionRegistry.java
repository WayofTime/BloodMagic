package WayofTime.bloodmagic.compress;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry aimed to help compress items in an inventory into its compressible
 * form.
 */
public class CompressionRegistry {
    public static Map<ItemStack, BaseCompressionHandler> compressionRegistry = new HashMap<>();
    public static AdvancedCompressionHandler advancedCompressionHandler = new AdvancedCompressionHandler();
    public static Map<ItemStack, Integer> thresholdMap = new HashMap<>();

    public static void registerBaseHandler(BaseCompressionHandler handler) {
        ItemStack standardized = handler.required.copy();
        standardized.setCount(1);
        compressionRegistry.put(standardized, handler);
    }

    /**
     * Registers an item so that it only compresses while above this threshold
     *
     * @param stack     item/block to be compressed
     * @param threshold amount that is to be compressed
     */
    public static void registerItemThreshold(ItemStack stack, int threshold) {
        thresholdMap.put(stack, threshold);
    }

    public static int getItemThreshold(ItemStack stack) {
        return stack.getItem().getItemStackLimit(stack); //this should work according to the guide, leaving behind a full stack of the source item (unless otherwise specified with a BaseCompressionHandler recipe)
    }

    public static boolean areItemStacksEqual(ItemStack stack, ItemStack compressedStack) {
        return stack.isItemEqual(compressedStack) && (stack.getTagCompound() == null ? !compressedStack.hasTagCompound() : stack.getTagCompound().equals(compressedStack.getTagCompound()));
    }
}