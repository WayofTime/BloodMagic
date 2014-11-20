package WayofTime.alchemicalWizardry.common.compress;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class DumbyInventoryCrafting extends InventoryCrafting 
{
	public DumbyInventoryCrafting(int x, int y) 
	{
		super(null, x, y);
	}

	@Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
    }
}
