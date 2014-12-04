package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class FireProjectile extends EnergyBlastProjectile
{
    public FireProjectile(World par1World)
    {
        super(par1World);
    }

    public FireProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public FireProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World, par2EntityPlayer, damage);
    }

    public FireProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public FireProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
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
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    for (int k = -1; k <= 1; k++)
                    {
                        if (worldObj.isAirBlock((int) this.posX + i, (int) this.posY + j, (int) this.posZ + k))
                        {
                            worldObj.setBlock((int) this.posX + i, (int) this.posY + j, (int) this.posZ + k, Blocks.fire);
                        }
                    }
                }
            }
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity mop)
    {
        if (mop == shootingEntity && ticksInAir > 3)
        {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else
        {
            if (mop instanceof EntityLivingBase)
            {
                ((EntityLivingBase) mop).setFire(10 * this.projectileDamage);
                ((EntityLivingBase) mop).setRevengeTarget(shootingEntity);

                if (((EntityLivingBase) mop).isPotionActive(Potion.fireResistance) || ((EntityLivingBase) mop).isImmuneToFire())
                {
                    ((EntityLivingBase) mop).attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
                } else
                {
                    doDamage(projectileDamage, mop);
                    ((EntityLivingBase) mop).hurtResistantTime = 0;
                }
            }
        }

        if (worldObj.isAirBlock((int) this.posX, (int) this.posY, (int) this.posZ))
        {
            worldObj.setBlock((int) this.posX, (int) this.posY, (int) this.posZ, Blocks.fire);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }
}
