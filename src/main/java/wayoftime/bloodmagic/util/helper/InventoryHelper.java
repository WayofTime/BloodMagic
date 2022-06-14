package wayoftime.bloodmagic.util.helper;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class InventoryHelper
{

	/**
	 * Gets all items from all registered inventories.
	 * 
	 * @param player - The player who's inventories to check.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getAllInventories(Player player)
	{
		Map<String, Function<Player, NonNullList<ItemStack>>> inventoryProvider = BloodMagicAPI.INSTANCE.getInventoryProvider();
		NonNullList<ItemStack> inventory = NonNullList.create();

		inventoryProvider.forEach((identifier, provider) -> inventory.addAll(provider.apply(player)));

		return inventory;
	}
}
