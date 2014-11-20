package WayofTime.alchemicalWizardry.api.compress;

import net.minecraft.item.ItemStack;

public abstract class CompressionHandler 
{	
	public abstract ItemStack getResultStack();
	
	public abstract ItemStack getRequiredStack();
	
	/**
	 * Called to look at the inventory and syphons the required stack. Returns getResultStack if successful, and null if not.
	 * @param inv 	The inventory iterated through
	 * @return 		The result of the compression
	 */
	public abstract ItemStack compressInventory(ItemStack[] inv);
}
