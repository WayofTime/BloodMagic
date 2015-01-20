package WayofTime.alchemicalWizardry.api;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class RoutingFocusLogicModItems extends RoutingFocusLogic
{
	@Override
	public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack)
	{
		if(keyStack != null && checkedStack != null)
		{
			UniqueIdentifier keyId = GameRegistry.findUniqueIdentifierFor(keyStack.getItem());
			UniqueIdentifier checkedId = GameRegistry.findUniqueIdentifierFor(checkedStack.getItem());
			return previous && keyId.modId.equals(checkedId.modId);
		}
		
		return previous;
	}
}
