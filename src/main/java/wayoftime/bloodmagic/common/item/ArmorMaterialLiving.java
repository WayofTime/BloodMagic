package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

public class ArmorMaterialLiving implements ArmorMaterial {
    public static final ArmorMaterial INSTANCE = new ArmorMaterialLiving();

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return ArmorMaterials.DIAMOND.getDurabilityForType(type);
    }
    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return ArmorMaterials.IRON.getDefenseForType(type);
    }

    @Override
    public int getEnchantmentValue() {
        return ArmorMaterials.IRON.getEnchantmentValue();
    }

    @Override
    public net.minecraft.sounds.SoundEvent getEquipSound() {
        return ArmorMaterials.IRON.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ArmorMaterials.IRON.getRepairIngredient();
    }

    @Override
    public String getName() {
        return "livingarmour";
    }

    @Override
    public float getToughness() {
        return ArmorMaterials.IRON.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.IRON.getKnockbackResistance();
    }
}


