package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;


public class EntityBloodLightProjectile extends EnergyBlastProjectile
{
	public EntityBloodLightProjectile(World par1World)
	{
		super(par1World);
	}

	public EntityBloodLightProjectile(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
	}

	public EntityBloodLightProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
	{
		super(par1World, par2EntityPlayer, damage);
	}

	public EntityBloodLightProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
	{
		super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
	}

	public EntityBloodLightProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
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
			int sideHit = mop.sideHit;
			int blockX = mop.blockX;
			int blockY = mop.blockY;
			int blockZ = mop.blockZ;

			if (sideHit == 0 && worldObj.isAirBlock(blockX, blockY - 1, blockZ))
			{
				worldObj.setBlock(blockX, blockY - 1, blockZ, AlchemicalWizardry.blockBloodLight.blockID);
			}

			if (sideHit == 1 && worldObj.isAirBlock(blockX, blockY + 1, blockZ))
			{
				worldObj.setBlock(blockX, blockY + 1, blockZ, AlchemicalWizardry.blockBloodLight.blockID);
			}

			if (sideHit == 2 && worldObj.isAirBlock(blockX, blockY, blockZ - 1))
			{
				worldObj.setBlock(blockX, blockY, blockZ - 1 , AlchemicalWizardry.blockBloodLight.blockID);
			}

			if (sideHit == 3 && worldObj.isAirBlock(blockX, blockY, blockZ + 1))
			{
				worldObj.setBlock(blockX, blockY, blockZ + 1 , AlchemicalWizardry.blockBloodLight.blockID);
			}

			if (sideHit == 4 && worldObj.isAirBlock(blockX - 1, blockY, blockZ))
			{
				worldObj.setBlock(blockX - 1, blockY, blockZ, AlchemicalWizardry.blockBloodLight.blockID);
			}

			if (sideHit == 5 && worldObj.isAirBlock(blockX + 1, blockY, blockZ))
			{
				worldObj.setBlock(blockX + 1, blockY, blockZ, AlchemicalWizardry.blockBloodLight.blockID);
			}

			//worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
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
				//((EntityLivingBase)mop).addPotionEffect(new PotionEffect(Potion.weakness.id, 60,2));
				((EntityLivingBase)mop).setFire(50);
				((EntityLivingBase)mop).setRevengeTarget(shootingEntity);

				if (((EntityLivingBase)mop).isPotionActive(Potion.fireResistance) || ((EntityLivingBase)mop).isImmuneToFire())
				{
					((EntityLivingBase)mop).attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
				}
				else
				{
					doDamage(projectileDamage, mop);
					((EntityLivingBase)mop).hurtResistantTime = 0;
				}
			}

			//worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
		}

		if (worldObj.isAirBlock((int)posX, (int)posY, (int)posZ))
		{
			worldObj.setBlock((int)posX, (int)posY, (int)posZ, Block.fire.blockID);
		}

		spawnHitParticles("magicCrit", 8);
		setDead();
	}
}
