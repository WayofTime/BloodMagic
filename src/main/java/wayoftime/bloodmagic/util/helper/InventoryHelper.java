package wayoftime.bloodmagic.util.helper;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class InventoryHelper
{

	/**
	 * Gets all items from all registered inventories.
	 * 
	 * @param player - The player who's inventories to check.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getAllInventories(PlayerEntity player)
	{
		Map<String, Function<PlayerEntity, NonNullList<ItemStack>>> inventoryProvider = BloodMagicAPI.INSTANCE.getInventoryProvider();
		NonNullList<ItemStack> inventory = NonNullList.create();

		inventoryProvider.forEach((identifier, provider) -> inventory.addAll(provider.apply(player)));

		return inventory;
	}
}
