package wayoftime.bloodmagic.util.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class InventoryHelper
{
	public static final Comparator<Map.Entry<String, Function<Player, NonNullList<ItemStack>>>> OFFHAND_FIRST = (i1, i2) -> i1.getKey().equals("offHandInventory")
			? -1
			: 0;

	/**
	 * @param player     - Player who's inventories you want.
	 * @param comparator - Sort order.
	 * @return A NonNullList of all items in inventories registered using the BM
	 *         API.
	 */
	public static NonNullList<ItemStack> getAllInventories(Player player, Comparator<Map.Entry<String, Function<Player, NonNullList<ItemStack>>>> comparator)
	{
		NonNullList<ItemStack> inventory = NonNullList.create();
		List<Map.Entry<String, Function<Player, NonNullList<ItemStack>>>> inventoryProviders = new ArrayList<Entry<String, Function<Player, NonNullList<ItemStack>>>>(BloodMagicAPI.INSTANCE.getInventoryProvider().entrySet());

		if (comparator != null)
			inventoryProviders.sort(comparator);

		inventoryProviders.forEach(provider -> inventory.addAll(provider.getValue().apply(player)));

		return inventory;
	}

	// no sort order method.
	public static NonNullList<ItemStack> getAllInventories(Player player)
	{
		return getAllInventories(player, null);
	}
}
