package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.UUID;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaEarth;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class OmegaArmourEarth extends OmegaArmour
{
	public OmegaArmourEarth(int armorType) 
	{
		super(armorType);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaEarth.png";
    }
	
	@Override
	public ModelBiped getChestModel()
	{
		return new ModelOmegaEarth(1.0f, true, true, false, true);
	}
	
	@Override
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaEarth(0.5f, false, false, true, false);
	}
	
	@Override 
	public Multimap getItemAttributeModifiers() 
	{ 
		Multimap map = HashMultimap.create(); 
		map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(179618 /** Random number **/, armorType), "Armor modifier" + armorType, getKnockbackResist(), 0)); 
		return map; 
	} 

	public float getKnockbackResist()
	{
		return 0.25f;
	}
}
