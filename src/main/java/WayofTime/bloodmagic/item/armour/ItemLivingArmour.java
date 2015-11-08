package WayofTime.bloodmagic.item.armour;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.registry.ModItems;

public class ItemLivingArmour extends ItemArmor {

	public static String[] names = { "helmet", "chest", "legs", "boots" };
	public ItemLivingArmour(int armorType) {
		super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
		setUnlocalizedName(BloodMagic.MODID + ".livingArmour.");
		setMaxDamage(250);
		setCreativeTab(BloodMagic.tabBloodMagic);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (this == ModItems.livingArmourChest
				|| this == ModItems.livingArmourHelmet
				|| this == ModItems.livingArmourBoots) {
			return "bloodmagic:models/armor/boundArmour_layer_1.png";
		}

		if (this == ModItems.livingArmourLegs) {
			return "bloodmagic:models/armor/boundArmour_layer_2.png";
		} else {
			return null;
		}
	}
	
	@Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[armorType];
    }
}
