package WayofTime.alchemicalWizardry.common.entity.projectile;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class WindGustProjectile extends EnergyBlastProjectile
{
    public WindGustProjectile(World par1World)
    {
        super(par1World);
    }

    public WindGustProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public WindGustProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World, par2EntityPlayer, damage);
    }

    public WindGustProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }
    
    public WindGustProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
    {
        super(par1World, par2EntityLivingBase, par3EntityLivingBase, par4, par5, damage, maxTicksInAir);
    }

    @Override
    public DamageSource getDamageSource()
    {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    @Override
    public void onImpact(MovingObjectPosition mop)
    {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            if (mop.entityHit == shootingEntity)
            {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity mop)
    {
        if (mop == shootingEntity && ticksInAir > 3)
        {
            this.setDead();
        } else
        {
            if (mop instanceof EntityLivingBase)
            {
                ((EntityLivingBase) mop).motionX = this.motionX * 0.25*this.projectileDamage;
                ((EntityLivingBase) mop).motionY = 1.5;
                ((EntityLivingBase) mop).motionZ = this.motionZ * 0.25*this.projectileDamage;
            }
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles()
    {
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "mobSpell", posX, posY, posZ, 1.0F, 1.0F, 1.0F);
    }
}