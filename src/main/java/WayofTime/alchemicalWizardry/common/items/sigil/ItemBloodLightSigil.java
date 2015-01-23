package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityBloodLightProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBloodLightSigil extends EnergyItems implements IHolding
{
    private int tickDelay = 100;

    public ItemBloodLightSigil()
    {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(10);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.bloodlightsigil.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodLightSigil");
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par2EntityPlayer);
        if(!EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed()))
        {
        	return true;
        }

        if (par3World.isRemote)
        {
            return true;
        }

        if (par7 == 0 && par3World.isAirBlock(par4, par5 - 1, par6))
        {
            par3World.setBlock(par4, par5 - 1, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 1 && par3World.isAirBlock(par4, par5 + 1, par6))
        {
            par3World.setBlock(par4, par5 + 1, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 2 && par3World.isAirBlock(par4, par5, par6 - 1))
        {
            par3World.setBlock(par4, par5, par6 - 1, ModBlocks.blockBloodLight);
        }

        if (par7 == 3 && par3World.isAirBlock(par4, par5, par6 + 1))
        {
            par3World.setBlock(par4, par5, par6 + 1, ModBlocks.blockBloodLight);
        }

        if (par7 == 4 && par3World.isAirBlock(par4 - 1, par5, par6))
        {
            par3World.setBlock(par4 - 1, par5, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 5 && par3World.isAirBlock(par4 + 1, par5, par6))
        {
            par3World.setBlock(par4 + 1, par5, par6, ModBlocks.blockBloodLight);
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if(!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed() * 5))
        {
        	return par1ItemStack;
        }

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EntityBloodLightProjectile(par2World, par3EntityPlayer, 10));
        }

        return par1ItemStack;
    }
}
