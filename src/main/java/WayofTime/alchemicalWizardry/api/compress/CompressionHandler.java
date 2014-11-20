package WayofTime.alchemicalWizardry.api.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class CompressionHandler 
{		
	/**
	 * Called to look at the inventory and syphons the required stack. Returns resultant stack if successful, and null if not.
	 * @param inv 	The inventory iterated through
	 * @return 		The result of the compression
	 */
	public abstract ItemStack compressInventory(ItemStack[] inv, World world);
}
