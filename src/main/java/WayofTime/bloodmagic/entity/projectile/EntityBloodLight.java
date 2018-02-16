package WayofTime.bloodmagic.entity.projectile;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

public class EntityBloodLight extends EntityThrowable implements IThrowableEntity, IEntityAdditionalSpawnData {
    public EntityLivingBase shootingEntity;
    protected int ticksInAir = 0;
    protected int maxTicksInAir = 600;

    public EntityBloodLight(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityBloodLight(World world, double x, double y, double z) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
    }

    public EntityBloodLight(World world, EntityLivingBase player) {
        super(world, player);
        shootingEntity = player;
        float par3 = 0.8F;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        this.shoot(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
    }

    @Override
    protected float getGravityVelocity() {
        return 0F;
    }

    @Override
    public void shoot(double var1, double var3, double var5, float var7, float var8) {
        float var9 = MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= var9;
        var3 /= var9;
        var5 /= var9;
        var1 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var3 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var5 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var1 *= var7;
        var3 *= var7;
        var5 *= var7;
        motionX = var1;
        motionY = var3;
        motionZ = var5;
        float var10 = MathHelper.sqrt(var1 * var1 + var5 * var5);
        prevRotationYaw = rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(var3, var10) * 180.0D / Math.PI);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > this.maxTicksInAir) {
            setDead();
        }
    }

    @Override
    protected void onImpact(RayTraceResult mop) {
        if (mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumFacing sideHit = mop.sideHit;
            BlockPos blockPos = mop.getBlockPos().offset(sideHit);

            if (getEntityWorld().isAirBlock(blockPos)) {
                getEntityWorld().setBlockState(blockPos, RegistrarBloodMagicBlocks.BLOOD_LIGHT.getDefaultState());
            }
        }

        this.setDead();
    }

    protected void onImpact(Entity mop) {
        if (mop == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else {
            if (mop instanceof EntityLivingBase) {
                ((EntityLivingBase) mop).setRevengeTarget(shootingEntity);
                doDamage(1, mop);
            }
        }

        if (getEntityWorld().isAirBlock(new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ))) {
            getEntityWorld().setBlockState(new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), Blocks.FIRE.getDefaultState());
        }

        // spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    protected void doDamage(int i, Entity mop) {
        mop.attackEntityFrom(this.getDamageSource(), i);
    }

    public DamageSource getDamageSource() {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeByte(this.ticksInAir);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        this.ticksInAir = data.readByte();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger(Constants.NBT.PROJECTILE_TICKS_IN_AIR, ticksInAir);
        nbt.setInteger(Constants.NBT.PROJECTILE_MAX_TICKS_IN_AIR, maxTicksInAir);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        ticksInAir = nbt.getInteger(Constants.NBT.PROJECTILE_TICKS_IN_AIR);
        maxTicksInAir = nbt.getInteger(Constants.NBT.PROJECTILE_MAX_TICKS_IN_AIR);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase)
            this.shootingEntity = (EntityLivingBase) entity;
    }
}
