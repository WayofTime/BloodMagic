package wayoftime.bloodmagic.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;

public class ItemLivingArmor extends ArmorItem implements ILivingContainer, ExpandedArmor
{

	private static final int MAX_ABSORPTION = 100000;

	public ItemLivingArmor(EquipmentSlotType slot)
	{
		super(ArmorMaterialLiving.INSTANCE, slot, new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
//		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
		modifiers.putAll(super.getAttributeModifiers(slot, stack));
		if (slot != EquipmentSlotType.CHEST)
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
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (!isInGroup(group))
			return;

		ItemStack stack = new ItemStack(this);
		if (slot == EquipmentSlotType.CHEST)
			updateLivingStats(stack, new LivingStats());

		items.add(stack);
	}

//	@Override
//	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
//	{
//		if (this.isInGroup(group))
//		{
//			for (EnumDemonWillType type : EnumDemonWillType.values())
//			{
//				ItemStack stack = new ItemStack(this);
//				this.setCurrentType(type, stack);
//				this.setWill(type, stack, maxWill);
//				items.add(stack);
//			}
//		}
//	}

	@Override
	public void damageArmor(LivingEntity livingEntity, ItemStack stack, DamageSource source, float damage, EquipmentSlotType slot)
	{
		if (slot == EquipmentSlotType.CHEST && damage > getMaxDamage() - stack.getDamage())
		{
//			livingEntity.attackEntityFrom(source, amount)
//		}
			livingEntity.attackEntityFrom(DamageSource.MAGIC, 2.0F);
			return;
		}

//        stack.damage((int) damage, livingEntity, entity -> {});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		ILivingContainer.appendLivingTooltip(getLivingStats(stack), tooltip, true);
	}
}
