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
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaEarth;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OmegaArmourEarth extends OmegaArmour
{
	private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;
    
	public OmegaArmourEarth(int armorType) 
	{
		super(armorType);
		this.storeYLevel = true;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaEarth.png";
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getChestModel()
	{
		return new ModelOmegaEarth(1.0f, true, true, false, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaEarth(0.5f, false, false, true, false);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaHelmet_earth");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaPlate_earth");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaLeggings_earth");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaBoots_earth");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.boundHelmetEarth))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.boundPlateEarth))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.boundLeggingsEarth))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.boundBootsEarth))
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
		map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(179618 /** Random number **/, armorType), "Knockback modifier" + armorType, getKnockbackResist(), 0)); 
		map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(80532 /** Random number **/, armorType), "Health modifier" + armorType, getDefaultArmourBoost()*getHealthBoostModifierForLevel(yLevel), 1)); 
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85112 /** Random number **/, armorType), "Damage modifier" + armorType, getDefaultArmourBoost()*getDamageModifierForLevel(yLevel), 2));		
		
		return map; 
	} 

	public float getKnockbackResist()
	{
		return 0.25f;
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
		return 0.05f * (yLevel <= 50 ? 1.5f : yLevel >= 100 ? -0.5f : 0.5f);
	}
	
	public float getDamageModifierForLevel(int yLevel)
	{
		return 0.03f * (yLevel <= 50 ? 1.5f : yLevel >= 100 ? -0.5f : 0.5f);
	}
}
