package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaWater;

public class OmegaArmourWater extends OmegaArmour
{
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
	public Multimap getAttributeModifiers(ItemStack stack)
    {
		Multimap map = HashMultimap.create();
		int biomeID = this.getBiomeIDStored(stack);
		BiomeGenBase biome = BiomeGenBase.getBiome(biomeID);
		if(biome != null)
		{
			map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85312 /** Random number **/, armorType), "Armor modifier" + armorType, getDefaultHealthBoost()*getHealthBoostModifierForBiome(biome), 0)); 
		}
		return map; 
    }
	
	public float getDefaultHealthBoost()
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
		if(biome.isEqualTo(BiomeGenBase.hell))
		{
			return -0.5f;
		}
		
		if(biome.isEqualTo(BiomeGenBase.ocean))
		{
			return 2.0f;
		}
		
		if(biome.isHighHumidity())
		{
			return 1.5f;
		}
		
		return 0.5f;
	}
}
