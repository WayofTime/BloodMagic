package WayofTime.alchemicalWizardry.api;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RoutingFocusParadigm 
{
	public List<RoutingFocusLogic> logicList = new LinkedList();
	
	public List<RoutingFocusPosAndFacing> locationList = new LinkedList();
	
	public int maximumAmount = 0;
	
	public void addRoutingFocusPosAndFacing(RoutingFocusPosAndFacing facing)
	{
		locationList.add(facing);
	}
	
	public void addLogic(RoutingFocusLogic logic)
	{
		if(logic instanceof ILimitingLogic)
		{
			maximumAmount += ((ILimitingLogic)logic).getRoutingLimit();
		}
		logicList.add(logic);
	}
	
	public boolean doesItemMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		boolean isGood = false;
		boolean isFirst = true;
		for(RoutingFocusLogic logic : logicList)
		{
			if(isFirst)
			{
				isGood = logic.getDefaultMatch(keyStack, checkedStack);
				isFirst = false;
				continue;
			}
			isGood = logic.doesItemMatch(isGood, keyStack, checkedStack);
		}
		
		return isGood;
	}
	
	public void clear()
	{
		logicList.clear();
		locationList.clear();
		maximumAmount = 0;
	}
	
	public void setMaximumAmount(int amt)
	{
		this.maximumAmount = amt;
	}
	
	public int getMaximumAmount()
	{
		return this.maximumAmount;
	}
}
