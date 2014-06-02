package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodboundSword extends EnergyItems
{
    private float weaponDamage;
    //private int maxMode = 3;
    private NBTTagCompound data;

    public BloodboundSword(int id)
    {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setEnergyUsed(100);
        setFull3D();
        weaponDamage = 10.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergySword");
    }

    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        if (par3EntityLivingBase instanceof EntityPlayer)
        {
            EnergyItems.checkAndSetItemOwner(par1ItemStack, (EntityPlayer) par3EntityLivingBase);

            if (!this.syphonBatteries(par1ItemStack, (EntityPlayer) par3EntityLivingBase, this.getEnergyUsed()))
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
    public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta)
    {
        if (par2Block.equals(Blocks.web))
        {
            return 15.0F;
        } else
        {
            Material material = par2Block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
        }
    }

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