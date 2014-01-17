package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;

public class EntityMeteor extends EnergyBlastProjectile
{
	private int meteorID;

	public EntityMeteor(World par1World)
	{
		super(par1World);
		meteorID = 0;
	}

	public EntityMeteor(World par1World, double par2, double par4, double par6, int meteorID)
	{
		super(par1World, par2, par4, par6);
		this.meteorID = meteorID;
	}

	@Override
	public DamageSource getDamageSource()
	{
		return DamageSource.fallingBlock;
	}

	@Override
	public void onImpact(MovingObjectPosition mop)
	{
		if (worldObj.isRemote)
		{
			return;
		}

		if (mop.typeOfHit == EnumMovingObjectType.ENTITY && mop.entityHit != null)
		{
			this.onImpact(mop.entityHit);
		}
		else if (mop.typeOfHit == EnumMovingObjectType.TILE)
		{
			MeteorRegistry.createMeteorImpact(worldObj, mop.blockX, mop.blockY, mop.blockZ, meteorID);
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
			MeteorRegistry.createMeteorImpact(worldObj, (int)posX, (int)posY, (int)posZ, meteorID);
		}

		setDead();
	}
}
