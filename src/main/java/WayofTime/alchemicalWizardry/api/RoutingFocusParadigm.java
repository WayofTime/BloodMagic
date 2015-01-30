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
		logicList.add(logic);
	}
	
	public boolean doesItemMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		boolean isGood = false;
		for(RoutingFocusLogic logic : logicList)
		{
			isGood = logic.doesItemMatch(isGood, keyStack, checkedStack);
			if(isGood){return true;}
		}
		
		return isGood;
	}
	
	public void clear()
	{
		logicList.clear();
		locationList.clear();
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
