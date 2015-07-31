package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.biome.BiomeGenBase;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaWater;

public class OmegaArmourWater extends OmegaArmour
{
	@SideOnly(Side.CLIENT)
	private IIcon helmetIcon;
	@SideOnly(Side.CLIENT)
    private IIcon plateIcon;
	@SideOnly(Side.CLIENT)
    private IIcon leggingsIcon;
	@SideOnly(Side.CLIENT)
    private IIcon bootsIcon;
    
	public OmegaArmourWater(int armorType) 
	{
		super(armorType);
		this.storeBiomeID = true;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaWater.png";
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getChestModel()
	{
		return new ModelOmegaWater(1.0f, true, true, false, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaWater(0.5f, false, false, true, false);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaHelmet_water");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaPlate_water");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaLeggings_water");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaBoots_water");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.boundHelmetWater))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.boundPlateWater))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.boundLeggingsWater))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.boundBootsWater))
        {
            return this.bootsIcon;
        }

        return this.itemIcon;
    }
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
    {
		Multimap map = HashMultimap.create();
		int biomeID = this.getBiomeIDStored(stack);
		BiomeGenBase biome = BiomeGenBase.getBiome(biomeID);
		if(biome != null)
		{
			map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85312 /** Random number **/, armorType), "Health modifier" + armorType, getDefaultArmourBoost()*getHealthBoostModifierForBiome(biome), 2));
			map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85432 /** Random number **/, armorType), "Damage modifier" + armorType, getDefaultArmourBoost()*getDamageModifierForBiome(biome), 2));			
		}
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
	
	public float getHealthBoostModifierForBiome(BiomeGenBase biome)
	{
		float modifier = 0.05f;
		if(biome.isEqualTo(BiomeGenBase.hell))
		{
			return modifier * -0.5f;
		}
		
		if(biome.isEqualTo(BiomeGenBase.ocean) || biome.isEqualTo(BiomeGenBase.river))
		{
			return modifier * 2.0f;
		}
		
		if(biome.isHighHumidity())
		{
			return modifier * 1.5f;
		}
		
		return modifier * 0.5f;
	}
	
	public float getDamageModifierForBiome(BiomeGenBase biome)
	{
		float modifier = 0.03f;
		
		if(biome.isEqualTo(BiomeGenBase.hell))
		{
			return modifier * -0.5f;
		}
		
		if(biome.isEqualTo(BiomeGenBase.ocean) || biome.isEqualTo(BiomeGenBase.river))
		{
			return modifier * 2.0f;
		}
		
		if(biome.isHighHumidity())
		{
			return modifier * 1.5f;
		}
		
		return modifier * 0.5f;
	}
}
