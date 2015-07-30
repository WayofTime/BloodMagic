package WayofTime.alchemicalWizardry.common.entity.projectile;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class WaterProjectile extends EnergyBlastProjectile
{
    public WaterProjectile(World par1World)
    {
        super(par1World);
    }

    public WaterProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public WaterProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World, par2EntityPlayer, damage);
    }

    public WaterProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
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
        }// else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
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
                if (mop.isImmuneToFire())
                {
                    doDamage(projectileDamage * 2, mop);
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 80, 1));
                } else
                {
                    doDamage(projectileDamage, mop);
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 80, 0));
                }
            }
        }

        spawnHitParticles(EnumParticleTypes.CRIT_MAGIC, 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles()
    {
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.getDimensionId(), EnumParticleTypes.PORTAL, posX, posY, posZ, -motionX, -motionY, -motionZ);
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.getDimensionId(), EnumParticleTypes.SPELL_MOB_AMBIENT, posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
    }
}
