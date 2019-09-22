package WayofTime.bloodmagic.entity.projectile;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySoulSnare extends ThrowableEntity {

    public EntitySoulSnare(World worldIn) {
        super(worldIn);
    }

    public EntitySoulSnare(World worldIn, LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntitySoulSnare(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit == this.getThrower() || this.ticksExisted < 2 || getEntityWorld().isRemote)
            return;

        if (result.entityHit instanceof LivingEntity) {
            if (result.entityHit.getEntityWorld().rand.nextDouble() < 0.25)
                ((LivingEntity) result.entityHit).addPotionEffect(new EffectInstance(RegistrarBloodMagic.SOUL_SNARE, 300, 0));

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) 0);
        }

        for (int j = 0; j < 8; ++j)
            this.getEntityWorld().spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

        this.setDead();
    }
}