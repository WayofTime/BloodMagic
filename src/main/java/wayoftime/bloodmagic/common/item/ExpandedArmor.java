package wayoftime.bloodmagic.common.item;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;

public interface ExpandedArmor
{

//	Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack);

	void damageArmor(LivingEntity livingEntity, ItemStack stack, DamageSource source, float damage, EquipmentSlot slot);

	Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack);
}
