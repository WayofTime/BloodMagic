package WayofTime.alchemicalWizardry.common.routing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import cpw.mods.fml.common.registry.GameData;

public class RoutingFocusLogicModItems extends RoutingFocusLogic
{
	@Override
	public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		if(keyStack != null && checkedStack != null && keyStack.getItem() != null && checkedStack.getItem() != null)
		{
			String keyId = getModID(keyStack.getItem());
			String checkedId = getModID(checkedStack.getItem());
			return keyId.equals(checkedId);
		}
		
		return false;
	}
	
	public String getModID(Item itm)
	{
		String str = GameData.getItemRegistry().getNameForObject(itm);
		if(!str.isEmpty())
		{
			String[] strs = str.split(":");
			if(strs != null && strs.length >= 1)
			{
				return strs[0];
			}
		}
		
		return "";
	}
}
