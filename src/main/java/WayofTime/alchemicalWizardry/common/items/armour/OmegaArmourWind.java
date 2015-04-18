package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.UUID;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaWind;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OmegaArmourWind extends OmegaArmour
{
	private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;
	
	public OmegaArmourWind(int armorType) 
	{
		super(armorType);
		this.storeYLevel = true;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaWind.png";
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getChestModel()
	{
		return new ModelOmegaWind(1.0f, true, true, false, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaWind(0.5f, false, false, true, false);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaHelmet_wind");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaPlate_wind");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaLeggings_wind");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaBoots_wind");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.boundHelmetWind))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.boundPlateWind))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.boundLeggingsWind))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.boundBootsWind))
        {
            return this.bootsIcon;
        }

        return this.itemIcon;
    }
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
    {
		Multimap map = HashMultimap.create();
		int yLevel = this.getYLevelStored(stack);
		map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85212 /** Random number **/, armorType), "Health modifier" + armorType, getDefaultArmourBoost()*getHealthBoostModifierForLevel(yLevel), 1)); 
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(86212 /** Random number **/, armorType), "Damage modifier" + armorType, getDefaultArmourBoost()*getDamageModifierForLevel(yLevel), 2));
		
		return map; 
    }
	
	public float getDefaultArmourBoost()
	{
		switch(this.armorType)
		{
		case 0:
			return 2.5f;
		case 1:
			return 4;
		case 2:
			return 3.5f;
		case 3:
			return 2;
		}
		return 0.25f;
	}
	
	public float getHealthBoostModifierForLevel(int yLevel)
	{
		return 0.05f * ((((float)yLevel)/64f) * 1.5f - 1);
	}
	
	public float getDamageModifierForLevel(int yLevel)
	{
		return 0.02f * ((((float)yLevel)/64f) * 1.5f - 1);
	}
}
