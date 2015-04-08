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
