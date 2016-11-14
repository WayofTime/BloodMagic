package WayofTime.bloodmagic.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import WayofTime.bloodmagic.registry.ModPotions;

public class EntitySoulSnare extends EntityThrowable
{
    public EntitySoulSnare(World worldIn)
    {
        super(worldIn);
    }

    public EntitySoulSnare(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntitySoulSnare(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit == this.getThrower() && this.ticksExisted < 20)
        {
            return;
        }

        if (result.entityHit != null && result.entityHit != this.getThrower())
        {
            if (result.entityHit instanceof EntityLivingBase && result.entityHit.worldObj.rand.nextDouble() < 0.25)
            {
                ((EntityLivingBase) result.entityHit).addPotionEffect(new PotionEffect(ModPotions.soulSnare, 300, 0));
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) 0);
        }

        for (int j = 0; j < 8; ++j)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}