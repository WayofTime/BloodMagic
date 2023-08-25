package wayoftime.bloodmagic.common.item;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class ItemLivingArmor extends ArmorItem implements ILivingContainer, ExpandedArmor, IForgeItem
{

	private static final int MAX_ABSORPTION = 100000;

	public ItemLivingArmor(ArmorItem.Type type)
	{
		super(ArmorMaterialLiving.INSTANCE, type, new Item.Properties().stacksTo(1));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		if (this == BloodMagicItems.LIVING_PLATE.get() || this == BloodMagicItems.LIVING_HELMET.get() || this == BloodMagicItems.LIVING_BOOTS.get())
		{
			return "bloodmagic:models/armor/livingarmour_layer_1.png";
		}

		if (this == BloodMagicItems.LIVING_LEGGINGS.get())
		{
			return "bloodmagic:models/armor/livingarmour_layer_2.png";
		} else
		{
			return null;
		}
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
	{
		return repair.getItem() == BloodMagicItems.REAGENT_BINDING.get() || super.isValidRepairItem(toRepair, repair);
	}

//	@Override
//	public void setDamage(ItemStack stack, int damage)
//	{
//		this.damageItem(stack, amount, entity, onBroken)
//		if (this.slot != EquipmentSlotType.CHEST)
//		{
//			return;
//		}
//		if (damage >= this.getMaxDamage(stack))
//		{
//			super.setDamage(stack, this.getMaxDamage(stack) - 1);
//		}
//	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken)
	{
		if (this != BloodMagicItems.LIVING_PLATE.get())
		{
			return super.damageItem(stack, amount, entity, onBroken);
		}

		int durRemaining = (stack.getMaxDamage() - 1 - stack.getDamageValue());
		int value = Math.max(Math.min(durRemaining, amount), 0);

//		System.out.println("value: " + value + ", damage of stack: " + stack.getDamage() + ", max damage of stack: " + stack.getMaxDamage());
		return value;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
//		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
		modifiers.putAll(super.getAttributeModifiers(slot, stack));
		if (slot != EquipmentSlot.CHEST)
			return modifiers;

		if (this.getMaxDamage(stack) - this.getDamage(stack) <= 1)
			return modifiers;

		LivingStats stats = getLivingStats(stack);
		if (stats == null)
			return modifiers;

		stats.getUpgrades().forEach((k, v) -> {
			if (k.getAttributeProvider() != null)
				k.getAttributeProvider().handleAttributes(stats, modifiers, UUID.nameUUIDFromBytes(k.getKey().toString().getBytes()), k, k.getLevel(v.intValue()));
		});

		return modifiers;
	}

//    @Override
//    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
//        if (source == DamageSource.DROWN || source == DamageSource.OUT_OF_WORLD)
//            return new ArmorProperties(-1, 0D, 0);
//
//        double armorReduction;
//        double damageAmount = 0.25;
//        double armorPenetrationReduction = 0;
//
//        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_FEET || armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_HEAD)
//            damageAmount = 3d / 20d * 0.6;
//        else if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_LEGS)
//            damageAmount = 6d / 20d * 0.6;
//        else if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST)
//            damageAmount = 0.64;
//
//        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST) {
//            armorReduction = 0.24 / 0.64; // This values puts it at iron level
//
//            if (!LivingUtil.hasFullSet((EntityPlayer) player))
//                return new ArmorProperties(-1, damageAmount * armorReduction, MAX_ABSORPTION);
//
//            LivingStats stats = getLivingStats(armor);
//            double protection = 1.0D;
//            if (stats != null)
//                for (Map.Entry<LivingUpgrade, Integer> entry : stats.getUpgrades().entrySet())
//                    if (entry.getKey().getArmorProvider() != null)
//                        protection *= 1 - entry.getKey().getArmorProvider().getProtection((EntityPlayer) player, stats, source, entry.getKey().getLevel(entry.getValue()));
//
//            armorReduction += (1 - protection) * (1 - armorReduction);
//            damageAmount *= armorReduction;
//            return new ArmorProperties(-1, source.isUnblockable() ? 1 - protection : damageAmount, MAX_ABSORPTION);
//        } else if (source.isUnblockable())
//            return new ArmorProperties(-1, damageAmount * armorPenetrationReduction, MAX_ABSORPTION);
//
//        return new ArmorProperties(-1, damageAmount, MAX_ABSORPTION);
//    }

	@Override
	public void damageArmor(LivingEntity livingEntity, ItemStack stack, DamageSource source, float damage, EquipmentSlot slot)
	{
		if (slot == EquipmentSlot.CHEST && damage > getMaxDamage() - stack.getDamageValue())
		{
//			livingEntity.attackEntityFrom(source, amount)
//		}
			livingEntity.hurt(livingEntity.damageSources().magic(), 2.0F);
			return;
		}

//        stack.damage((int) damage, livingEntity, entity -> {});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		ILivingContainer.appendLivingTooltip(stack, getLivingStats(stack), tooltip, true);
	}

	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		return hasElytraUpgrade(stack, entity) && stack.getDamageValue() < stack.getMaxDamage() - 1;
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		if (!entity.level().isClientSide && (flightTicks + 1) % 40 == 0)
			stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
		return true;
	}

	public boolean hasElytraUpgrade(ItemStack stack, LivingEntity entity)
	{
		if (stack.getItem() instanceof ItemLivingArmor && entity instanceof Player && LivingUtil.hasFullSet((Player) entity))
			return LivingStats.fromPlayer((Player) entity, true).getLevel(LivingArmorRegistrar.UPGRADE_ELYTRA.get().getKey()) > 0;
		else
			return false;
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
	{
		if (stack.getItem() instanceof ItemLivingArmor && wearer instanceof Player && LivingUtil.hasFullSet((Player) wearer)) {
			return LivingStats.fromPlayer((Player) wearer, true).getLevel(LivingArmorRegistrar.UPGRADE_GILDED.get().getKey()) > 0;
		}
		return false;
	}
}
