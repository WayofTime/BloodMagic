package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

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
		}

		setDead();
	}

	@Override
	public void onImpact(Entity mop)
	{
		if (mop == shootingEntity && ticksInAir > 3)
		{
			//shootingEntity.attackEntityFrom(DamageSource.causePlayerDamage(shootingEntity), 1);
			setDead();
		}
		else
		{
			//doDamage(8 + d6(), mop);
			if (mop instanceof EntityLivingBase)
			{
				//((EntityLivingBase)mop).addPotionEffect(new PotionEffect(Potion.weakness.id, 60,2));
				//((EntityLivingBase)mop).setFire(50);
				//((EntityLivingBase)mop).setRevengeTarget(shootingEntity);
				//        		if(((EntityLivingBase)mop).isEntityUndead())
				//        		{
				//        			doDamage((int)(projectileDamage*2),mop);
				//        		}else
				//        		{
				//        			doDamage(projectileDamage, mop);
				//        		}
				((EntityLivingBase)mop).motionX = motionX * 2;
				((EntityLivingBase)mop).motionY = 1.5;
				((EntityLivingBase)mop).motionZ = motionZ * 2;
				//((EntityLivingBase)mop).setVelocity(this.motionX*2, ((EntityLivingBase)mop).motionY+1.5, this.motionZ*2);
			}

			//worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
		}

		spawnHitParticles("magicCrit", 8);
		setDead();
	}

	@Override
	public void doFiringParticles()
	{
		PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 30, worldObj.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D));
		PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 30, worldObj.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", posX, posY, posZ, 1.0F, 1.0F, 1.0F));
	}
}