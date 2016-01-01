package WayofTime.bloodmagic.api.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry aimed to help compress items in an inventory into its compressible
 * form.
 */
public class CompressionRegistry
{
    public static List<CompressionHandler> compressionRegistry = new ArrayList<CompressionHandler>();
    public static Map<ItemStack, Integer> thresholdMap = new HashMap<ItemStack, Integer>();

    public static void registerHandler(CompressionHandler handler)
    {
        compressionRegistry.add(handler);
    }

    /**
     * Registers an item so that it only compresses while above this threshold
     * 
     * @param stack
     *            item/block to be compressed
     * @param threshold
     *            amount that is to be compressed
     */
    public static void registerItemThreshold(ItemStack stack, int threshold)
    {
        thresholdMap.put(stack, threshold);
    }

    public static ItemStack compressInventory(ItemStack[] inv, World world)
    {
        for (CompressionHandler handler : compressionRegistry)
        {
            ItemStack stack = handler.compressInventory(inv, world);
            if (stack != null)
            {
                return stack;
            }
        }

        return null;
    }

    public static int getItemThreshold(ItemStack stack)
    {
        for (Map.Entry<ItemStack, Integer> entry : thresholdMap.entrySet())
        {
            if (areItemStacksEqual(entry.getKey(), stack))
            {
                return entry.getValue();
            }
        }

        return 0;
    }

    public static boolean areItemStacksEqual(ItemStack stack, ItemStack compressedStack)
    {
        return stack.isItemEqual(compressedStack) && (stack.getTagCompound() == null ? compressedStack.getTagCompound() == null : stack.getTagCompound().equals(compressedStack.getTagCompound()));
    }
}
