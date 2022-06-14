package wayoftime.bloodmagic.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;

public class ArmorMaterialLiving implements IArmorMaterial
{
	public static final IArmorMaterial INSTANCE = new ArmorMaterialLiving();

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slot)
	{
		return ArmorMaterial.DIAMOND.getDurabilityForSlot(slot);
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slot)
	{
		return ArmorMaterial.IRON.getDefenseForSlot(slot);
	}

	@Override
	public int getEnchantmentValue()
	{
		return ArmorMaterial.IRON.getEnchantmentValue();
	}

	@Override
	public net.minecraft.util.SoundEvent getEquipSound()
	{
		return ArmorMaterial.IRON.getEquipSound();
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return ArmorMaterial.IRON.getRepairIngredient();
	}

	@Override
	public String getName()
	{
		return "livingarmour";
	}

	@Override
	public float getToughness()
	{
		return ArmorMaterial.IRON.getToughness();
	}

	@Override
	public float getKnockbackResistance()
	{
		return ArmorMaterial.IRON.getKnockbackResistance();
	}
}