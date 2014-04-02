package WayofTime.alchemicalWizardry.api.altarRecipeRegistry;

import net.minecraft.item.ItemStack;

public class AltarRecipe 
{
	public int minTier;
	public int liquidRequired;
	public boolean canBeFilled; //Tells the system that the item is an orb
	public int consumptionRate;
	public int drainRate;
	public ItemStack requiredItem;
	public ItemStack result;
	
	public AltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
	{
		this.result = result;
		this.requiredItem = requiredItem;
		this.minTier = minTier;
		this.liquidRequired = liquidRequired;
		this.consumptionRate = consumptionRate;
		this.drainRate = drainRate;
		this.canBeFilled = canBeFilled;
	}
	
	public ItemStack getResult()
	{
		return this.result;
	}
	
	public ItemStack getRequiredItem()
	{
		return this.requiredItem;
	}
	
	public boolean doesRequiredItemMatch(ItemStack comparedStack, int tierCheck)
	{
		if(comparedStack == null || this.requiredItem == null)
		{
			return false;
		}
		
		return tierCheck>=minTier && this.requiredItem.isItemEqual(comparedStack);
	}
	
	public int getMinTier()
	{
		return this.minTier;
	}
	
	public int getLiquidRequired()
	{
		return this.liquidRequired;
	}
	
	public int getConsumptionRate()
	{
		return this.consumptionRate;
	}
	
	public int getDrainRate()
	{
		return this.drainRate;
	}
	
	public boolean getCanBeFilled()
	{
		return this.canBeFilled;
	}
}
