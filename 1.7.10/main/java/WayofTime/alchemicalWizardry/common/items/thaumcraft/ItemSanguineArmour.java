package WayofTime.alchemicalWizardry.common.items.thaumcraft;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSanguineArmour extends ItemArmor implements ArmourUpgrade, IGoggles, IVisDiscountGear, IRevealer, IRunicArmor, IRepairable
{
	private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;

    public ItemSanguineArmour(int armorType)
    {
        super(AlchemicalWizardry.sanguineArmourArmourMaterial, 0, armorType);
        setMaxDamage(1000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineHelmet");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPlate");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundLeggings");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundBoots");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.sanguineHelmet))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.sanguineRobe))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.sanguinePants))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.sanguineBoots))
        {
            return this.bootsIcon;
        }

        return this.itemIcon;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if (this == ModItems.sanguineHelmet)
        {
            return "alchemicalwizardry:models/armor/sanguineArmour_layer_1.png";
        }
        
        if (this == ModItems.sanguineRobe || this == ModItems.sanguineBoots)
        {
            return "alchemicalwizardry:models/armor/boundArmour_layer_1.png";
        }

        if (this == ModItems.sanguinePants)
        {
            return "alchemicalwizardry:models/armor/boundArmour_layer_2.png";
        } else
        {
            return null;
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	int discount = 0;
    	
    	switch(this.armorType)
		{
		case 0: 
			discount = 8;
			break;
		case 1: 
			discount = 4;
			break;
		case 2: 
			discount = 3;
			break;
		case 3: 
			discount = 3;
			break;
		}
    	
    	switch(this.armorType)
		{
		case 0: 
	        par3List.add("A pair of goggles imbued with power");
			break;
		case 1: 

		case 2: 

		case 3: 
	        par3List.add("Robes imbued with forbidden power");
		}
    	
        par3List.add("Vis discount: " + discount + "%");
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        return;
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) 
	{
		return true;
	}

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) 
	{
		switch(this.armorType)
		{
		case 0: return 8;
		case 1: return 4;
		case 2: return 3;
		case 3: return 3;
		}
		return 0;
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) 
	{
		return true;
	}

	@Override
	public int getRunicCharge(ItemStack itemstack) {
		// TODO Auto-generated method stub
		return 0;
	}
}