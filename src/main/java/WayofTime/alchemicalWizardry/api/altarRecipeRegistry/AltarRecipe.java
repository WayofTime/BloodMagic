package WayofTime.alchemicalWizardry.api.altarRecipeRegistry;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class AltarRecipe
{
    public int minTier;
    public int liquidRequired;
    public boolean canBeFilled; //Tells the system that the item is an orb
    public int consumptionRate;
    public int drainRate;
    public ItemStack requiredItem;
    public ItemStack result;
    public boolean useTag;

    public AltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
    {
        this(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled, false);
    }
    
    public AltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled, boolean useTag)
    {
    	this.result = result;
        this.requiredItem = requiredItem;
        this.minTier = minTier;
        this.liquidRequired = liquidRequired;
        this.consumptionRate = consumptionRate;
        this.drainRate = drainRate;
        this.canBeFilled = canBeFilled;
        this.useTag = useTag;
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
        if (comparedStack == null || this.requiredItem == null)
        {
            return false;
        }

        return tierCheck >= minTier && this.requiredItem.isItemEqual(comparedStack) && (this.useTag ? this.areRequiredTagsEqual(comparedStack) : true);
    }
    
    public boolean areRequiredTagsEqual(ItemStack comparedStack)
    {
    	if(requiredItem.hasTagCompound())
    	{
        	NBTTagCompound tag = requiredItem.getTagCompound();
        	if(!comparedStack.hasTagCompound())
        	{
        		return false;
        	}
        	
        	NBTTagCompound comparedTag = comparedStack.getTagCompound();
        	
        	return this.areTagsEqual(tag, comparedTag);
    	}
    	
    	return true;
    }
    
    protected boolean areTagsEqual(NBTTagCompound tag, NBTTagCompound comparedTag)
    {
    	Set set = tag.func_150296_c();
    	
    	for(Object obj : set)
    	{
    		if(obj instanceof String)
    		{
    			String str = (String)obj;
    			
    			NBTBase baseTag = comparedTag.getTag(str);
    			
    			if(baseTag instanceof NBTTagCompound)
    			{
    				NBTBase comparedBaseTag = comparedTag.getTag(str);
    				if(comparedBaseTag instanceof NBTTagCompound)
    				{
    					if(!this.areTagsEqual((NBTTagCompound) tag, comparedTag))
    					{
    						return false;
    					}
    				}
    			}else
    			{
    				if(baseTag != null && !baseTag.equals(comparedTag.getTag(str)))
        			{
        				return false;
        			}
    			}	
    		}
    	}
    	
    	return true;
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
