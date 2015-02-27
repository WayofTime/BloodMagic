package WayofTime.alchemicalWizardry.common.routing;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class RoutingFocusLogicModItems extends RoutingFocusLogic
{
	@Override
	public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		if(keyStack != null && checkedStack != null)
		{
			UniqueIdentifier keyId = GameRegistry.findUniqueIdentifierFor(keyStack.getItem());
			UniqueIdentifier checkedId = GameRegistry.findUniqueIdentifierFor(checkedStack.getItem());
			return keyId.modId.equals(checkedId.modId);
		}
		
		return false;
	}
}
