package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;


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
        	EnumFacing facing = mop.field_178784_b;
        	BlockPos position = mop.func_178782_a().offset(facing);
            
            if(this.worldObj.isAirBlock(position))
            {
            	this.worldObj.setBlockState(position, ModBlocks.blockBloodLight.getDefaultState());
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
                ((EntityLivingBase) mop).setRevengeTarget(shootingEntity);
                doDamage(1, mop);
            }
        }

        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
        if (worldObj.isAirBlock(pos))
        {
            worldObj.setBlockState(pos, Blocks.fire.getDefaultState());
        }

        spawnHitParticles(EnumParticleTypes.CRIT_MAGIC, 8);
        this.setDead();
    }
}
