package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaFire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OmegaArmourFire extends OmegaArmour
{
	private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;
    
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
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaHelmet_fire");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaPlate_fire");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaLeggings_fire");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:OmegaBoots_fire");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.boundHelmetFire))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.boundPlateFire))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.boundLeggingsFire))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.boundBootsFire))
        {
            return this.bootsIcon;
        }

        return this.itemIcon;
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
