package WayofTime.alchemicalWizardry.api.compress;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * A registry aimed to help compress items in an inventory into its compressible form.
 *
 */
public class CompressionRegistry 
{
	public static List<CompressionHandler> compressionRegistry = new ArrayList();
	
	public static void registerHandler(CompressionHandler handler)
	{
		compressionRegistry.add(handler);
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
}
