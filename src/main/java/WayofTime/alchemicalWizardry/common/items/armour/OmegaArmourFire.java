package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaFire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OmegaArmourFire extends OmegaArmour
{
	public OmegaArmourFire(int armorType) 
	{
		super(armorType);
//		this.storeYLevel = true;
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
	
//	@Override
//	public Multimap getAttributeModifiers(ItemStack stack)
//    {
//		Multimap map = HashMultimap.create();
//		
////		map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85212 /** Random number **/, armorType), "Armor modifier" + armorType, getDefaultHealthBoost()*getHealthBoostModifierForLevel(yLevel), 0)); 
//		
//		return map; 
//    }
	
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
	
//	public float getHealthBoostModifierForLevel(int yLevel)
//	{
//		return (float)Math.sqrt(((float)yLevel)/64f) * 1.5f - 1;
//	}
}
