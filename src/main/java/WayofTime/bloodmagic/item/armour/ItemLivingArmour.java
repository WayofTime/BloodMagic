package WayofTime.bloodmagic.item.armour;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.registry.ModItems;

import com.google.common.collect.Multimap;

public class ItemLivingArmour extends ItemArmor
{
    public static String[] names = { "helmet", "chest", "legs", "boots" };

    public ItemLivingArmour(int armorType)
    {
        super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
        setUnlocalizedName(Constants.Mod.MODID + ".livingArmour.");
        setMaxDamage(250);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if (this == ModItems.livingArmourChest || this == ModItems.livingArmourHelmet || this == ModItems.livingArmourBoots)
        {
            return "bloodmagic:models/armor/boundArmour_layer_1.png";
        }

        if (this == ModItems.livingArmourLegs)
        {
            return "bloodmagic:models/armor/boundArmour_layer_2.png";
        } else
        {
            return null;
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        if (this == ModItems.livingArmourChest)
        {
            LivingArmour armour = getLivingArmour(stack);

            return armour.getAttributeModifiers();
        }

        return super.getAttributeModifiers(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[armorType];
    }

    public LivingArmour getLivingArmour(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        NBTTagCompound livingTag = tag.getCompoundTag(Constants.NBT.LIVING_ARMOUR);

        LivingArmour livingArmour = new LivingArmour();
        livingArmour.readFromNBT(livingTag);

        return livingArmour;
    }
}
