package WayofTime.alchemicalWizardry.api.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * A registry aimed to help compress items in an inventory into its compressible form.
 *
 */
public class CompressionRegistry 
{
	public static List<CompressionHandler> compressionRegistry = new ArrayList();
	public static Map<ItemStack, Integer> thresholdMap = new HashMap();
	
	public static void registerHandler(CompressionHandler handler)
	{
		compressionRegistry.add(handler);
	}
	
	/**
	 * Registers an item so that it only compresses while above this threshold
	 * @param stack
	 * @param threshold
	 */
	public static void registerItemThreshold(ItemStack stack, int threshold)
	{
		thresholdMap.put(stack, new Integer(threshold));
	}
	
	public static ItemStack compressInventory(ItemStack[] inv, World world)
	{
		for(CompressionHandler handler : compressionRegistry)
		{
			ItemStack stack = handler.compressInventory(inv, world);
			if(stack != null)
			{
				return stack;
			}
		}
		
		return null;
	}
	
	public static int getItemThreshold(ItemStack stack)
	{
		for(Entry<ItemStack, Integer> entry : thresholdMap.entrySet())
		{
			if(areItemStacksEqual(entry.getKey(), stack))
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
