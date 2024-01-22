package wayoftime.bloodmagic.util.helper;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

	/**
	 * Gets all items from all registered inventories marked as active as well as
	 * main and off hand
	 * 
	 * @param player - The player who's inventories to check.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getActiveInventories(Player player)
	{
		Map<String, Function<Player, NonNullList<ItemStack>>> inventoryProviders = BloodMagicAPI.INSTANCE.getActiveInventoryProvider();
		NonNullList<ItemStack> inventories = NonNullList.create();

		inventoryProviders.forEach((identifier, provider) -> inventories.addAll(provider.apply(player)));

		inventories.add(player.getItemInHand(InteractionHand.MAIN_HAND));
		inventories.add(player.getItemInHand(InteractionHand.OFF_HAND));

		return inventories;
	}
}
