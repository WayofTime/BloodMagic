package WayofTime.alchemicalWizardry.api.sacrifice;

import net.minecraft.item.ItemStack;

public interface IIncense 
{
	int getMinLevel(ItemStack stack);
	
	int getMaxLevel(ItemStack stack);
	
	int getIncenseDuration(ItemStack stack);
		  
	/**
	 * @param stack
	 * @return a float from 0 to 1
	 */
	float getRedColour(ItemStack stack);
	float getGreenColour(ItemStack stack);
	float getBlueColour(ItemStack stack);
	float getTickRate(ItemStack stack);
}
