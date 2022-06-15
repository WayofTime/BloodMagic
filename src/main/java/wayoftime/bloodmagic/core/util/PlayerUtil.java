package wayoftime.bloodmagic.core.util;

import java.util.function.Predicate;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.ExpandedArmor;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class PlayerUtil
{

	public static ItemStack findItem(Player player, Predicate<ItemStack> requirements)
	{

		// Check offhand first
		ItemStack offHand = player.getOffhandItem();
		if (requirements.test(offHand))
			return offHand;

		// Check inventory next
		for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
		{
			ItemStack foundStack = player.getInventory().getItem(slot);
			if (!foundStack.isEmpty() && requirements.test(foundStack))
				return foundStack;
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
