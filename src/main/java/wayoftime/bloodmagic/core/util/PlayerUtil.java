package wayoftime.bloodmagic.core.util;

import java.util.function.Predicate;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.common.item.ExpandedArmor;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class PlayerUtil
{

	public static ItemStack findItem(PlayerEntity player, Predicate<ItemStack> requirements)
	{

		// Check offhand first
		ItemStack offHand = player.getOffhandItem();
		if (requirements.test(offHand))
			return offHand;

		// Check inventory next
		for (int slot = 0; slot < player.inventory.getContainerSize(); slot++)
		{
			ItemStack foundStack = player.inventory.getItem(slot);
			if (!foundStack.isEmpty() && requirements.test(foundStack))
				return foundStack;
		}

		return ItemStack.EMPTY;
	}

	public static Multimap<Attribute, AttributeModifier> handle(PlayerEntity player, Multimap<Attribute, AttributeModifier> existing)
	{

		ItemStack chest = player.getItemBySlot(EquipmentSlotType.CHEST);
		boolean hasFullSet = LivingUtil.hasFullSet(player);

		if (hasFullSet && existing == null)
		{
			existing = ((ExpandedArmor) chest.getItem()).getAttributeModifiers(EquipmentSlotType.CHEST, chest);
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
