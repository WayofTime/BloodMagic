package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.UUID;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaFire;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class OmegaArmourFire extends OmegaArmour
{   
	public OmegaArmourFire(int armorType) 
	{
		super(armorType);
		this.storeBiomeID = true;
		this.illegalEnchantmentList.add(Enchantment.fireProtection);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaFire.png";
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getChestModel()
	{
		return new ModelOmegaFire(1.0f, true, true, false, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaFire(0.5f, false, false, true, false);
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
    {
		Multimap map = HashMultimap.create();
		int biomeID = this.getBiomeIDStored(stack);
		BiomeGenBase biome = BiomeGenBase.getBiome(biomeID);
		if(biome != null)
		{
			map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(895132 /** Random number **/, armorType), "Health modifier" + armorType, getDefaultArmourBoost()*getHealthBoostModifierForBiome(biome), 1));
			map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(196312 /** Random number **/, armorType), "Damage modifier" + armorType, getDefaultArmourBoost()*getDamageModifierForBiome(biome), 1)); 
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
			return modifier * 2.0f;
		}
		
		if(biome.isEqualTo(BiomeGenBase.ocean))
		{
			return modifier * -0.5f;
		}
		
		if(biome.temperature >= 1)
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
			return modifier * 2.0f;
		}
		
		if(biome.isEqualTo(BiomeGenBase.ocean))
		{
			return modifier * -0.5f;
		}
		
		if(biome.temperature >= 1)
		{
			return modifier * 1.5f;
		}
		
		return modifier * 0.5f;
	}
}
