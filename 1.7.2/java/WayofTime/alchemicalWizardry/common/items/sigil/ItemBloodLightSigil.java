package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityBloodLightProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBloodLightSigil extends EnergyItems
{
    private int tickDelay = 100;

    public ItemBloodLightSigil()
    {
        super();
        this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(10);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("I see a light!");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
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
        EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());

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

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed() * 5);

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EntityBloodLightProjectile(par2World, par3EntityPlayer, 10));
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par3Entity instanceof EntityPlayer)
            {
                EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, getEnergyUsed());
            }
        }

        return;
    }
}
