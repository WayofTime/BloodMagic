package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnergyBazookaMainProjectile extends EnergyBlastProjectile
{
    public EntityEnergyBazookaMainProjectile(World par1World)
    {
        super(par1World);
    }

    public EntityEnergyBazookaMainProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public EntityEnergyBazookaMainProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World, par2EntityPlayer, damage);
    }

    public EntityEnergyBazookaMainProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public EntityEnergyBazookaMainProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
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
            worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, (float) (5.0f), false);
            this.spawnSecondaryProjectiles();
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
                spawnSecondaryProjectiles();
            }

            worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, (float) (5.0f), false);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    public void spawnSecondaryProjectiles()
    {
        for (int i = 0; i < 20; i++)
        {
            EntityEnergyBazookaSecondaryProjectile secProj = new EntityEnergyBazookaSecondaryProjectile(worldObj, this.posX, this.posY, this.posZ, 15);
            secProj.shootingEntity = this.shootingEntity;
            float xVel = rand.nextFloat() - rand.nextFloat();
            float yVel = rand.nextFloat() - rand.nextFloat();
            float zVel = rand.nextFloat() - rand.nextFloat();
            float wantedVel = 0.5f;
            secProj.motionX = xVel * wantedVel;
            secProj.motionY = yVel * wantedVel;
            secProj.motionZ = zVel * wantedVel;
            worldObj.spawnEntityInWorld(secProj);
        }
    }
}
