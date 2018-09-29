package com.wayoftime.bloodmagic.item;

import com.google.common.collect.Multimap;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicItems;
import com.wayoftime.bloodmagic.core.living.ILivingContainer;
import com.wayoftime.bloodmagic.core.living.LivingStats;
import com.wayoftime.bloodmagic.core.living.LivingUpgrade;
import com.wayoftime.bloodmagic.core.living.LivingUtil;
import com.wayoftime.bloodmagic.core.network.Binding;
import com.wayoftime.bloodmagic.core.network.SoulNetwork;
import com.wayoftime.bloodmagic.core.network.NetworkInteraction;
import com.wayoftime.bloodmagic.core.util.BooleanResult;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemLivingArmor extends ItemArmor implements ILivingContainer, ISpecialArmor, IBindable {

    public static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("living", "living", 100, new int[]{1, 2, 3, 4}, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    private static final int MAX_ABSORPTION = 100000;

    public ItemLivingArmor(EntityEquipmentSlot slot) {
        super(MATERIAL, 0, slot);

        setTranslationKey(BloodMagic.MODID + ":living_armor_" + slot.getName());
        setRegistryName("living_armor_" + slot.getName());
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (getBinding(stack) == null)
            IBindable.applyBinding(stack, player);

        super.onArmorTick(world, player, stack);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
        if (slot != EntityEquipmentSlot.CHEST)
            return modifiers;

        LivingStats stats = getLivingStats(stack);
        if (stats == null)
            return modifiers;

        stats.getUpgrades().forEach((k, v) -> {
            if (k.getAttributeProvider() != null)
                k.getAttributeProvider().handleAttributes(stats, modifiers, k.getLevel(v));
        });

        return modifiers;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        if (source == DamageSource.DROWN || source == DamageSource.OUT_OF_WORLD)
            return new ArmorProperties(-1, 0D, 0);

        double armorReduction;
        double damageAmount = 0.25;
        double armorPenetrationReduction = 0;

        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_FEET || armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_HEAD)
            damageAmount = 3d / 20d * 0.6;
        else if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_LEGS)
            damageAmount = 6d / 20d * 0.6;
        else if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST)
            damageAmount = 0.64;

        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST) {
            armorReduction = 0.24 / 0.64; // This values puts it at iron level

            if (!LivingUtil.hasFullSet((EntityPlayer) player))
                return new ArmorProperties(-1, damageAmount * armorReduction, MAX_ABSORPTION);

            LivingStats stats = getLivingStats(armor);
            double protection = 1.0D;
            if (stats != null)
                for (Map.Entry<LivingUpgrade, Integer> entry : stats.getUpgrades().entrySet())
                    if (entry.getKey().getArmorProvider() != null)
                        protection *= 1 - entry.getKey().getArmorProvider().getProtection((EntityPlayer) player, stats, source, entry.getKey().getLevel(entry.getValue()));

            armorReduction += (1 - protection) * (1 - armorReduction);
            damageAmount *= armorReduction;
            return new ArmorProperties(-1, source.isUnblockable() ? 1 - protection : damageAmount, MAX_ABSORPTION);
        } else if (source.isUnblockable())
            return new ArmorProperties(-1, damageAmount * armorPenetrationReduction, MAX_ABSORPTION);

        return new ArmorProperties(-1, damageAmount, MAX_ABSORPTION);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_HEAD)
            return 3;
        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST)
            return 8;
        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_LEGS)
            return 6;
        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_FEET)
            return 3;

        return 0;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (this == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST || this == RegistrarBloodMagicItems.LIVING_ARMOR_HEAD || this == RegistrarBloodMagicItems.LIVING_ARMOR_FEET)
            return "bloodmagic:textures/models/armor/living_armor_layer_1.png";

        if (this == RegistrarBloodMagicItems.LIVING_ARMOR_LEGS)
            return "bloodmagic:textures/models/armor/living_armor_layer_2.png";

        return null;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        if (stack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOR_CHEST && damage > getMaxDamage(stack) - getDamage(stack)) {
            Binding binding = getBinding(stack);
            if (binding == null) {
                entity.attackEntityFrom(SoulNetwork.WEAK_SOUL, 2.0F);
                return;
            }

            BooleanResult<Integer> result = SoulNetwork.get(binding.getOwnerId()).submitInteraction(NetworkInteraction.asItemInfo(stack, entity.getEntityWorld(), entity, damage * 100).syphon());
            if (!result.isSuccess())
                entity.attackEntityFrom(SoulNetwork.WEAK_SOUL, 2.0F);

            return;
        }

        stack.damageItem(damage, entity);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ILivingContainer.appendLivingTooltip(getLivingStats(stack), tooltip, true);
        IBindable.appendTooltip(getBinding(stack), tooltip);
    }
}
