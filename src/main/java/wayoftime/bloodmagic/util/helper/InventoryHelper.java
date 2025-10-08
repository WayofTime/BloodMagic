package wayoftime.bloodmagic.util.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class InventoryHelper
{
	private static Map<String, Function<Player, NonNullList<ItemStack>>> inventoryProvider = BloodMagicAPI.INSTANCE.getInventoryProvider();

	/**
	 * Gets all items from the specified inventory.
	 *
	 * @param player - The player who's inventories to check.
	 * @param inventoryKey - inventory's name. See BloodMagicCorePlugin for the vanilla ones.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getInventory(Player player, String inventoryKey)
	{
		NonNullList<ItemStack> inventory = NonNullList.create();
		inventory.addAll(inventoryProvider.get(inventoryKey).apply(player));
		return inventory;
	}

	/**
	 * Gets all items from all registered inventories.
	 * 
	 * @param player - The player who's inventories to check.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getAllInventories(Player player)
	{
		NonNullList<ItemStack> inventory = NonNullList.create();

		inventoryProvider.forEach((identifier, provider) -> inventory.addAll(provider.apply(player)));

		return inventory;
	}

	/**
	 * Gets all items from all inventories, excluding the listed inventories
	 *
	 * @param player - The player who's inventories to check.
	 * @param excludedInventoryKeys - inventory keys to exclude. See BloodMagicCorePlugin for the vanilla ones.
	 * @return - NonNullList<ItemStack> of all items in those inventories.
	 */
	public static NonNullList<ItemStack> getAllInventoriesExcluding(Player player, List<String> excludedInventoryKeys)
	{
		NonNullList<ItemStack> inventory = NonNullList.create();

		inventoryProvider.forEach((identifier, provider) -> {if (!identifier.equals(excludedInventoryKeys)) {inventory.addAll(provider.apply(player));}});

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
		Map<String, Function<Player, NonNullList<ItemStack>>> activeInventoryProvider = BloodMagicAPI.INSTANCE.getActiveInventoryProvider();
		NonNullList<ItemStack> inventories = NonNullList.create();

		activeInventoryProvider.forEach((identifier, provider) -> inventories.addAll(provider.apply(player)));

		inventories.add(player.getItemInHand(InteractionHand.MAIN_HAND));
		inventories.add(player.getItemInHand(InteractionHand.OFF_HAND));

		return inventories;
	}
}
