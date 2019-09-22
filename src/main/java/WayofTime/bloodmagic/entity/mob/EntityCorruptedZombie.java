package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class EntityCorruptedZombie extends EntityAspectedDemonBase {
    private final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.0D, false);

    private final int attackPriority = 3;

    public EntityCorruptedZombie(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
//        ((PathNavigateGround) getNavigator()).setCanSwim(false);
        this.tasks.addTask(0, new SwimGoal(this));
        this.tasks.addTask(attackPriority, aiAttackOnCollide);
        this.tasks.addTask(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.tasks.addTask(7, new RandomWalkingGoal(this, 1.0D));
        this.tasks.addTask(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(8, new LookRandomlyGoal(this));

        this.tasks.addTask(6, new MoveThroughVillageGoal(this, 1.0D, false));
        this.targetTasks.addTask(1, new HurtByTargetGoal(this, true, ZombiePigmanEntity.class));
        this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetTasks.addTask(3, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, false));
        this.targetTasks.addTask(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));

        this.setCombatTask();
//        this.targetTasks.addTask(8, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 10, true, false, new TargetPredicate(this)));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    public void setCombatTask() {
        if (!this.getEntityWorld().isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (!itemstack.isEmpty() && itemstack.getItem() instanceof BowItem) {
                int i = 20;

                if (this.getEntityWorld().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(attackPriority, this.aiArrowAttack);
            } else {
                this.tasks.addTask(attackPriority, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }

    /**
     * Redone from EntityMob to prevent despawning on peaceful.
     */
    @Override
    public boolean attackEntityAsMob(Entity attackedEntity) {
        boolean flag = super.attackEntityAsMob(attackedEntity);

        if (flag) {
            //EMPTY

            return true;
        } else {
            return false;
        }
    }

    /**
     * @param toHeal
     * @return Amount of Will consumed from the Aura to heal
     */
    public double absorbWillFromAuraToHeal(double toHeal) {
        if (getEntityWorld().isRemote) {
            return 0;
        }

        double healthMissing = this.getMaxHealth() - this.getHealth();
        if (healthMissing <= 0) {
            return 0;
        }

        double will = WorldDemonWillHandler.getCurrentWill(getEntityWorld(), getPosition(), getType());

        toHeal = Math.min(healthMissing, Math.min(toHeal, will / getWillToHealth()));
        if (toHeal > 0) {
            this.heal((float) toHeal);
            return WorldDemonWillHandler.drainWill(getEntityWorld(), getPosition(), getType(), toHeal * getWillToHealth(), true);
        }

        return 0;
    }

    public double getWillToHealth() {
        return 2;
    }

    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && super.canDespawn();
    }

    public void onUpdate() {
        if (!this.getEntityWorld().isRemote && this.ticksExisted % 20 == 0) {
            absorbWillFromAuraToHeal(2);
        }

        super.onUpdate();
    }

    //TODO: Change to fit the given AI
    @Override
    public boolean shouldAttackEntity(LivingEntity attacker, LivingEntity owner) {
        return !(attacker instanceof CreeperEntity) && !(attacker instanceof GhastEntity) && super.shouldAttackEntity(attacker, owner);
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.5f;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
}