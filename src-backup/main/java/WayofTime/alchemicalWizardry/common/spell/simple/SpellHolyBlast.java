package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellHolyBlast extends HomSpell
{
    Random itemRand = new Random();

    public SpellHolyBlast()
    {
        super();
        this.setEnergies(100, 300, 500, 400);
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
            par2World.spawnEntityInWorld(new HolyProjectile(par2World, par3EntityPlayer, 8));
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

        int distance = 2;
        double yaw = par3EntityPlayer.rotationYaw / 180 * Math.PI;
        double pitch = par3EntityPlayer.rotationPitch / 180 * Math.PI;
        double xCoord = par3EntityPlayer.posX + Math.sin(yaw) * Math.cos(pitch) * (-distance);
        double yCoord = par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight() + Math.sin(-pitch) * distance;
        double zCoord = par3EntityPlayer.posZ + Math.cos(yaw) * Math.cos(pitch) * distance;
        float d0 = 0.5f;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(par3EntityPlayer.posX - 0.5 + Math.sin(yaw) * Math.cos(pitch) * (-distance), par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight() + Math.sin(-pitch) * distance, par3EntityPlayer.posZ - 0.5 + Math.cos(yaw) * Math.cos(pitch) * distance, par3EntityPlayer.posX + Math.sin(yaw) * Math.cos(pitch) * (-distance) + 0.5, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight() + Math.sin(-pitch) * distance + 1, par3EntityPlayer.posZ + Math.cos(yaw) * Math.cos(pitch) * distance + 0.5).expand(d0, d0, d0);
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

            int i = 1;

            if (entityLiving.isEntityUndead())
            {
                i = 3;
            }

            //entityLiving.setFire(50);
            entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(par3EntityPlayer), 5 * i);
            //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);
        }

        par2World.createExplosion(par3EntityPlayer, xCoord, yCoord, zCoord, (float) (1), false);

        for (int i = 0; i < 5; i++)
        {
            //PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + itemRand.nextFloat() - itemRand.nextFloat(), yCoord + itemRand.nextFloat() - itemRand.nextFloat(), zCoord + itemRand.nextFloat() - itemRand.nextFloat(), 1.0F, 1.0F, 1.0F));
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + itemRand.nextFloat() - itemRand.nextFloat(), yCoord + itemRand.nextFloat() - itemRand.nextFloat(), zCoord + itemRand.nextFloat() - itemRand.nextFloat(), 1.0F, 1.0F, 1.0F);
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

        if (!par2World.isRemote)
        {
            for (int i = 0; i < 360; i += 18)
            {
                par2World.spawnEntityInWorld(new HolyProjectile(par2World, par3EntityPlayer, 8, 3, par3EntityPlayer.posX, par3EntityPlayer.posY + (par3EntityPlayer.height / 2), par3EntityPlayer.posZ, i, 0));
            }
        }

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

        int d0 = 3;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) par3EntityPlayer.posX, (double) par3EntityPlayer.posY, (double) par3EntityPlayer.posZ, (double) (par3EntityPlayer.posX + 1), (double) (par3EntityPlayer.posY + 2), (double) (par3EntityPlayer.posZ + 1)).expand(d0, d0, d0);
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

            int i = 1;

            if (entityLiving.isEntityUndead())
            {
                i = 3;
            }

            //entityLiving.setFire(50);
            entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(par3EntityPlayer), 5 * i);
            //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);
        }

        par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float) (2), false);
        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;

        for (int i = 0; i < 20; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + itemRand.nextFloat() - itemRand.nextFloat(), yCoord + itemRand.nextFloat() - itemRand.nextFloat(), zCoord + itemRand.nextFloat() - itemRand.nextFloat(), 1.0F, 1.0F, 1.0F);
        }

        return par1ItemStack;
    }
}
