package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.entity.ai.EntityAIAttackStealthMelee;
import WayofTime.bloodmagic.entity.ai.EntityAIStealthRetreat;
import WayofTime.bloodmagic.entity.ai.EntityAIStealthTowardsTarget;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCorruptedChicken extends EntityAspectedDemonBase {
    private final int attackPriority = 3;
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    /**
     * The time until the next egg is spawned.
     */
    public int timeUntilNextEgg;
    /*
     * 0 means the chicken is casting stealth on itself when targeted, running
     * to a random location near the target. 1 means the chicken is running
     * after the target, attempting to attack it. 2 means it is running away
     * from the target for a certain amount of time, before going back into
     * state 0.
     */
    public int attackStateMachine = 0;
    private EntityAIAttackStealthMelee aiAttackOnCollide;

    public EntityCorruptedChicken(World world) {
        this(world, EnumDemonWillType.DEFAULT);
    }

    public EntityCorruptedChicken(World world, EnumDemonWillType type) {
        super(world);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(600) + 600;
        this.setPathPriority(PathNodeType.WATER, 0.0F);

        this.setType(type);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(attackPriority, new EntityAIStealthTowardsTarget(this, 1));
        this.tasks.addTask(attackPriority, new EntityAIStealthRetreat(this, 6.0F, 1.4D, 1.4D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 10, true, false, new EntityAspectedDemonBase.TeamAttackPredicate(this)));
    }

    @Override
    public void setCombatTask() {
        if (aiAttackOnCollide != null) {
            this.tasks.removeTask(aiAttackOnCollide);
        }

        aiAttackOnCollide = new EntityAIAttackStealthMelee(this, this.getBaseSprintModifier(getType()), false);
        this.tasks.addTask(attackPriority, aiAttackOnCollide);
    }

    public void cloak() {
        this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 50, 0, false, false));
    }

    @Override
    public double getBaseHP(EnumDemonWillType type) {
        return super.getBaseHP(type) * 0.5;
    }

    @Override
    public double getBaseMeleeDamage(EnumDemonWillType type) {
        return super.getBaseMeleeDamage(type) * 2.5;
    }

    @Override
    public double getBaseSpeed(EnumDemonWillType type) {
        return super.getBaseSpeed(type);
    }

    @Override
    public double getBaseSprintModifier(EnumDemonWillType type) {
        return super.getBaseSprintModifier(type);
    }

    @Override
    public double getBaseKnockbackResist(EnumDemonWillType type) {
        return super.getBaseKnockbackResist(type);
    }

    @Override
    public float getEyeHeight() {
        return this.height;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

//        if (!worldObj.isRemote)
//            System.out.println("State machine: " + this.attackStateMachine);

        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.getEntityWorld().isRemote && !this.isChild() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.EGG, 1);
            this.timeUntilNextEgg = this.rand.nextInt(600) + 600;
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.5f;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
        float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        passenger.setPosition(this.posX + (double) (0.1F * f), this.posY + (double) (this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (double) (0.1F * f1));

        if (passenger instanceof EntityLivingBase) {
            ((EntityLivingBase) passenger).renderYawOffset = this.renderYawOffset;
        }
    }
}
