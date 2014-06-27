package WayofTime.alchemicalWizardry.common.spell.simple;

import ibxm.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.WindGustProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellWindGust extends HomSpell
{
    Random itemRand = new Random();

    public SpellWindGust()
    {
        super();
        this.setEnergies(300, 400, 300, 500);
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
            par2World.spawnEntityInWorld(new WindGustProjectile(par2World, par3EntityPlayer, 8));
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

        int distance = 3;
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

            //entityLiving.setFire(50);
            //entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(par3EntityPlayer), 5*i);
            //entityLiving.setVelocity(Math.sin(-yaw)*2, 2, Math.cos(yaw)*2);
            entityLiving.motionX = Math.sin(-yaw) * 2;
            entityLiving.motionY = 2;
            entityLiving.motionZ = Math.cos(yaw) * 2;
            //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);
        }

        //par2World.createExplosion(par3EntityPlayer, xCoord, yCoord, zCoord, (float)(1), false);

        for (int i = 0; i < 5; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);

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

        int distance = 3;
        double yaw = par3EntityPlayer.rotationYaw / 180 * Math.PI;
        double pitch = par3EntityPlayer.rotationPitch / 180 * Math.PI;
        double wantedVelocity = 5;
        double xVel = Math.sin(yaw) * Math.cos(pitch) * (-wantedVelocity);
        double yVel = Math.sin(-pitch) * wantedVelocity;
        double zVel = Math.cos(yaw) * Math.cos(pitch) * wantedVelocity;
        Vec3 vec = par3EntityPlayer.getLookVec();
        par3EntityPlayer.motionX = vec.xCoord * wantedVelocity;
        par3EntityPlayer.motionY = vec.yCoord * wantedVelocity;
        par3EntityPlayer.motionZ = vec.zCoord * wantedVelocity;
        //PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(xVel, yVel, zVel), (Player) par3EntityPlayer);
        SpellHelper.setPlayerSpeedFromServer(par3EntityPlayer, xVel, yVel, zVel);
        par2World.playSoundEffect((double) ((float) par3EntityPlayer.posX + 0.5F), (double) ((float) par3EntityPlayer.posY + 0.5F), (double) ((float) par3EntityPlayer.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
        par3EntityPlayer.fallDistance = 0;
        //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);
        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;

        for (int i = 0; i < 8; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
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
        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;
        double wantedVel = 2;

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

            double posXDif = entityLiving.posX - par3EntityPlayer.posX;
            double posYDif = entityLiving.posY - par3EntityPlayer.posY + 1;
            double posZDif = entityLiving.posZ - par3EntityPlayer.posZ;
            double distance2 = Math.pow(posXDif, 2) + Math.pow(posYDif, 2) + Math.pow(posZDif, 2);
            double distance = Math.sqrt(distance2);
            //entityLiving.setVelocity(posXDif*wantedVel/distance, posYDif*wantedVel/distance, posZDif*wantedVel/distance);
            entityLiving.motionX = posXDif * wantedVel / distance;
            entityLiving.motionY = posYDif * wantedVel / distance;
            entityLiving.motionZ = posZDif * wantedVel / distance;
            //entityLiving.setFire(50);
            //entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(par3EntityPlayer), 5*i);
            //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);
        }

        //par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, (float)(2), false);

        for (int i = 0; i < 20; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
        }

        return par1ItemStack;
    }
}