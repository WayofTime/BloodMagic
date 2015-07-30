package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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
                    	BlockPos newPos = new BlockPos(this.posX + i, this.posY + j, this.posZ + k);
                        if (worldObj.isAirBlock(newPos))
                        {
                            worldObj.setBlockState(newPos, Blocks.fire.getDefaultState());
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
                mop.setFire(10 * this.projectileDamage);
                ((EntityLivingBase) mop).setRevengeTarget(shootingEntity);

                if (((EntityLivingBase) mop).isPotionActive(Potion.fireResistance) || mop.isImmuneToFire())
                {
                    mop.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
                } else
                {
                    doDamage(projectileDamage, mop);
                    ((EntityLivingBase) mop).hurtResistantTime = 0;
                }
            }
        }

        BlockPos newPos = new BlockPos(this.posX, this.posY, this.posZ);
        if (worldObj.isAirBlock(newPos))
        {
            worldObj.setBlockState(newPos, Blocks.fire.getDefaultState());
        }

        spawnHitParticles(EnumParticleTypes.CRIT_MAGIC, 8);
        this.setDead();
    }
}
