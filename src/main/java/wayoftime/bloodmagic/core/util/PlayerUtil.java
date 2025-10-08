package wayoftime.bloodmagic.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Multimap;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ExpandedArmor;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.util.helper.InventoryHelper;

public class PlayerUtil
{

	public static ItemStack findItem(Player player, Predicate<ItemStack> requirements)
	{

		// Check offhand first
		ItemStack offHand = player.getOffhandItem();
		if (requirements.test(offHand))
			return offHand;
		List<String> checkedInventories = new ArrayList<>();
		checkedInventories.add("offHandInventory");

		// Check Curios next, if available.
		if (BloodMagic.curiosLoaded)
		{
			NonNullList<ItemStack> curiosInventory = InventoryHelper.getInventory(player, "curiosInventory");
			for (ItemStack item : curiosInventory)
			{
				if (requirements.test(item))
					return item;
			}
			checkedInventories.add("curiosInventory");
		}

		// Check Main Inventory next.
		NonNullList<ItemStack> mainInventory = InventoryHelper.getInventory(player, "mainInventory");
		for (ItemStack item : mainInventory)
		{
			if (requirements.test(item))
				return item;
		}
		checkedInventories.add("mainInventory");

		// Check all remaining registered inventories.  Armor and Add-ons.
		NonNullList<ItemStack> remainingInventories = InventoryHelper.getAllInventoriesExcluding(player, checkedInventories);
		for (ItemStack item : remainingInventories)
		{
			if (requirements.test(item))
				return item;
		}

		return ItemStack.EMPTY;
	}

	public static Multimap<Attribute, AttributeModifier> handle(Player player, Multimap<Attribute, AttributeModifier> existing)
	{

		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		boolean hasFullSet = LivingUtil.hasFullSet(player);

		if (hasFullSet && existing == null)
		{
			existing = ((ExpandedArmor) chest.getItem()).getAttributeModifiers(EquipmentSlot.CHEST, chest);
			player.getAttributes().addTransientAttributeModifiers(existing);
		}

		if (!hasFullSet && existing != null)
		{
			player.getAttributes().removeAttributeModifiers(existing);
			existing = null;
		}

		return existing;
	}
}
