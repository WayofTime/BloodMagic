package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityIceDemon;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class IceProjectile extends EnergyBlastProjectile
{
    public IceProjectile(World par1World)
    {
        super(par1World);
    }

    public IceProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public IceProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World, par2EntityPlayer, damage);
    }

    public IceProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public IceProjectile(World worldObj, EntityIceDemon entityIceDemon, EntityLivingBase par1EntityLivingBase, float f, float g, int i, int j)
    {
        super(worldObj, entityIceDemon, par1EntityLivingBase, f, g, i, j);
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
//        	for(int i=-1;i<=1;i++)
//        	{
//        		for(int j=-1;j<=1;j++)
//        		{
//        			for(int k=-1;k<=1;k++)
//        			{
//        				if(worldObj.isAirBlock((int)this.posX+i, (int)this.posY+j, (int)this.posZ+k))
//        	        	{
//        	        		worldObj.setBlock( (int)this.posX+i, (int)this.posY+j, (int)this.posZ+k,Block.fire.blockID);
//        	        	}
//        			}
//        		}
//        	}
            //worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
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
            //doDamage(8 + d6(), mop);
            if (mop instanceof EntityLivingBase)
            {
                //((EntityLivingBase)mop).addPotionEffect(new PotionEffect(Potion.weakness.id, 60,2));
                //((EntityLivingBase)mop).setFire(50);
                //((EntityLivingBase)mop).setRevengeTarget(shootingEntity);
                if (((EntityLivingBase) mop).isImmuneToFire())
                {
                    doDamage((int) (projectileDamage * 2), mop);
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2));
                } else
                {
                    doDamage(projectileDamage, mop);
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
                }
            }

            //worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
        }

        if (worldObj.isAirBlock((int) this.posX, (int) this.posY, (int) this.posZ))
        {
            //worldObj.setBlock((int)this.posX, (int)this.posY, (int)this.posZ,Block.fire.blockID);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles()
    {
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "explode", posX, posY, posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
    }
}
