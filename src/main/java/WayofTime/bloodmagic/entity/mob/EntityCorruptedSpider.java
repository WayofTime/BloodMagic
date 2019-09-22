package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.entity.ai.EntityAIPickUpAlly;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCorruptedSpider extends EntityAspectedDemonBase {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityCorruptedSpider.class, DataSerializers.BYTE);

    public EntityCorruptedSpider(World world) {
        this(world, EnumDemonWillType.DEFAULT);
    }

    public EntityCorruptedSpider(World world, EnumDemonWillType type) {
        super(world);
        this.setSize(1.4F, 0.9F);

        this.setType(type);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new SwimGoal(this));
        this.tasks.addTask(3, new LeapAtTargetGoal(this, 0.4F));
        this.tasks.addTask(3, new EntityAIPickUpAlly(this, 1, true));
        this.tasks.addTask(4, new EntityCorruptedSpider.AISpiderAttack(this));
        this.tasks.addTask(5, new RandomWalkingGoal(this, 0.8D));
        this.tasks.addTask(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(6, new LookRandomlyGoal(this));
        this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));

        this.targetTasks.addTask(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new EntityAspectedDemonBase.TeamAttackPredicate(this)));
    }

    @Override
    public double getBaseHP(EnumDemonWillType type) {
        return super.getBaseHP(type);
    }

    @Override
    public double getBaseMeleeDamage(EnumDemonWillType type) {
        return super.getBaseMeleeDamage(type);
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
    public double getMountedYOffset() {
        return (double) (this.height * 0.5F);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.getEntityWorld().isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.5f;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    @Override
    public void setInWeb() {
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    @Override
    public float getEyeHeight() {
        return 0.65F;
    }

    static class AISpiderAttack extends MeleeAttackGoal {
        public AISpiderAttack(EntityCorruptedSpider spider) {
            super(spider, 1.0D, true);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            float f = this.attacker.getBrightness();

            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (4.0F + attackTarget.width);
        }
    }

    static class AISpiderTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AISpiderTarget(EntityCorruptedSpider spider, Class<T> classTarget) {
            super(spider, classTarget, true);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            float f = this.taskOwner.getBrightness();
            return !(f >= 0.5F) && super.shouldExecute();
        }
    }
}