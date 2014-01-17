package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodboundSword extends EnergyItems
{
	public BloodboundSword(int id)
	{
		super(id);
		maxStackSize = 1;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setEnergyUsed(100);
		setFull3D();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergySword");
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if (par3EntityLivingBase instanceof EntityPlayer)
		{
			EnergyItems.checkAndSetItemOwner(par1ItemStack, (EntityPlayer)par3EntityLivingBase);

			if (!syphonBatteries(par1ItemStack, (EntityPlayer)par3EntityLivingBase, getEnergyUsed()))
			{
				//this.damagePlayer(null, (EntityPlayer)par3EntityLivingBase, (this.getEnergyUsed() + 99) / 100);
			}
		}

		return true;
	}

	/*
    public int getDamageVsEntity(Entity par1Entity)
    {
        return this.weaponDamage;
    }
	 */

	public float func_82803_g()
	{
		return 4.0F;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Caution: may cause");
		par3List.add("a bad day...");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
		}
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
	{
		if (par2Block.blockID == Block.web.blockID)
		{
			return 15.0F;
		}
		else
		{
			Material material = par2Block.blockMaterial;
			return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.pumpkin ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}

	//    public Multimap func_111205_h()
	//    {
	//        Multimap multimap = super.func_111205_h();
	//        multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.weaponDamage, 0));
	//        return multimap;
	//    }
}