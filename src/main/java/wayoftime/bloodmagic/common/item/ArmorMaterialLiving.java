package wayoftime.bloodmagic.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;

public class ArmorMaterialLiving implements IArmorMaterial
{
	public static final IArmorMaterial INSTANCE = new ArmorMaterialLiving();

	@Override
	public int getDurability(EquipmentSlotType slot)
	{
		return ArmorMaterial.IRON.getDurability(slot);
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot)
	{
		return ArmorMaterial.IRON.getDamageReductionAmount(slot);
	}

	@Override
	public int getEnchantability()
	{
		return ArmorMaterial.IRON.getEnchantability();
	}

	@Override
	public net.minecraft.util.SoundEvent getSoundEvent()
	{
		return ArmorMaterial.IRON.getSoundEvent();
	}

	@Override
	public Ingredient getRepairMaterial()
	{
		return ArmorMaterial.IRON.getRepairMaterial();
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