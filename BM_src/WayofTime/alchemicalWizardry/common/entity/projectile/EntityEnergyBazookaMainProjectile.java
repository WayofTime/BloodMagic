package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
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
		super(par1World,  par2EntityLivingBase, par3EntityLivingBase, par4, par5, damage, maxTicksInAir);
	}

	@Override
	public DamageSource getDamageSource()
	{
		return DamageSource.causeMobDamage(shootingEntity);
	}

	@Override
	public void onImpact(MovingObjectPosition mop)
	{
		if (mop.typeOfHit == EnumMovingObjectType.ENTITY && mop.entityHit != null)
		{
			if (mop.entityHit == shootingEntity)
			{
				return;
			}

			this.onImpact(mop.entityHit);
		}
		else if (mop.typeOfHit == EnumMovingObjectType.TILE)
		{
			worldObj.createExplosion(shootingEntity, posX, posY, posZ, 5.0f, false);
			spawnSecondaryProjectiles();
		}

		setDead();
	}

	@Override
	public void onImpact(Entity mop)
	{
		if (mop == shootingEntity && ticksInAir > 3)
		{
			shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
			setDead();
		}
		else
		{
			//doDamage(8 + d6(), mop);
			if (mop instanceof EntityLivingBase)
			{
				spawnSecondaryProjectiles();
			}

			worldObj.createExplosion(shootingEntity, posX, posY, posZ, 5.0f, false);
		}

		spawnHitParticles("magicCrit", 8);
		setDead();
	}

	public void spawnSecondaryProjectiles()
	{
		for (int i = 0; i < 20; i++)
		{
			EntityEnergyBazookaSecondaryProjectile secProj = new EntityEnergyBazookaSecondaryProjectile(worldObj, posX, posY, posZ, 15);
			secProj.shootingEntity = shootingEntity;
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
