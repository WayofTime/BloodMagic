package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellFireBurst extends HomSpell
{
    public Random itemRand = new Random();

    public SpellFireBurst()
    {
        super();
        this.setEnergies(100, 300, 400, 100);
        //this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveRangedEnergy());
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            //par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
        	FireProjectile proj = new FireProjectile(par2World, par3EntityPlayer, 7);
            par2World.spawnEntityInWorld(proj);
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveMeleeEnergy());
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    par2World.spawnEntityInWorld(new FireProjectile(par2World, par3EntityPlayer, 8, 2, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw + i * 10F, par3EntityPlayer.rotationPitch + j * 10F));
                }
            }
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getDefensiveEnergy());
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        int d0 = 2;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) par3EntityPlayer.posX, (double) par3EntityPlayer.posY, (double) par3EntityPlayer.posZ, (double) (par3EntityPlayer.posX + 1), (double) (par3EntityPlayer.posY + 2), (double) (par3EntityPlayer.posZ + 1)).expand(d0, d0, d0);
        //axisalignedbb.maxY = (double)this.worldObj.getHeight();
        List list = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        Iterator iterator = list.iterator();

        while (iterator.hasNext())
        {
            EntityLivingBase entityLiving = (EntityLivingBase) iterator.next();

            if (entityLiving instanceof EntityPlayer)
            {
                if (entityLiving.equals(par3EntityPlayer))
                {
                    continue;
                }
            }

            entityLiving.setFire(100);
            entityLiving.attackEntityFrom(DamageSource.inFire, 2);
        }

//        if (!par2World.isRemote)
//        {
//
//        	for(int i=0;i<10;i++)
//        	{
//        		for(int j=0;j<5;j++)
//        		{
//        			par2World.spawnEntityInWorld(new FireProjectile(par2World, par3EntityPlayer, 10,5,par3EntityPlayer.posX,par3EntityPlayer.posY+par3EntityPlayer.getEyeHeight(),par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw+i*36F,par3EntityPlayer.rotationPitch+j*72F));
//        		}
//        	}
//        }
        return par1ItemStack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getEnvironmentalEnergy());
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        World worldObj = par2World;

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int k = -1; k <= 1; k++)
                {
                    if (worldObj.isAirBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k))
                    {
                        if (worldObj.rand.nextFloat() < 0.8F)
                        {
                            worldObj.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k, Blocks.fire);
                        }
                    }
                }
            }
        }

        return par1ItemStack;
    }
}
