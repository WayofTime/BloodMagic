package WayofTime.alchemicalWizardry.api.sacrifice;

import net.minecraft.item.ItemStack;

public interface IIncense 
{
	public int getMinLevel(ItemStack stack);
	
	public int getMaxLevel(ItemStack stack);
	
	public int getIncenseDuration(ItemStack stack);
		  
	/**
	 * @param stack
	 * @return a float from 0 to 1
	 */
	public float getRedColour(ItemStack stack);
	public float getGreenColour(ItemStack stack);
	public float getBlueColour(ItemStack stack);
	float getTickRate(ItemStack stack);
}
