package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIAttackStealthMelee extends EntityAIBase {
    protected final int attackInterval = 20;
    protected EntityCorruptedChicken chicken;
    /**
     * An amount of decrementing ticks that allows the entity to attack once the
     * tick reaches 0.
     */
    protected int attackTick;
    World worldObj;
    /**
     * The speed with which the mob will approach the target
     */
    double speedTowardsTarget;
    /**
     * When true, the mob will continue chasing its target, even if it can't
     * find a path to them right now.
     */
    boolean longMemory;
    /**
     * The PathEntity of our entity.
     */
    Path entityPathEntity;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public EntityAIAttackStealthMelee(EntityCorruptedChicken creature, double speedIn, boolean useLongMemory) {
        this.chicken = creature;
        this.worldObj = creature.getEntityWorld();
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (chicken.attackStateMachine != 1) {
            return false;
        }

        EntityLivingBase entitylivingbase = this.chicken.getAttackTarget();

        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        } else {
            if (canPenalize) {
                if (--this.delayCounter <= 0) {
                    this.entityPathEntity = this.chicken.getNavigator().getPathToEntityLiving(entitylivingbase);
                    this.delayCounter = 4 + this.chicken.getRNG().nextInt(7);
                    return this.entityPathEntity != null;
                } else {
                    return true;
                }
            }
            this.entityPathEntity = this.chicken.getNavigator().getPathToEntityLiving(entitylivingbase);
            return this.entityPathEntity != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return chicken.attackStateMachine == 1 && super.shouldContinueExecuting();
    }

    @Override
    public void resetTask() {
        if (chicken.attackStateMachine == 1) {
            chicken.attackStateMachine = 0;
        }
    }

    @Override
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.chicken.getAttackTarget();
        this.chicken.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = this.chicken.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        --this.delayCounter;

        if ((this.longMemory || this.chicken.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.chicken.getRNG().nextFloat() < 0.05F)) {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.chicken.getRNG().nextInt(7);

            if (this.canPenalize) {
                this.delayCounter += failedPathFindingPenalty;
                if (this.chicken.getNavigator().getPath() != null) {
                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.chicken.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                        failedPathFindingPenalty = 0;
                    else
                        failedPathFindingPenalty += 10;
                } else {
                    failedPathFindingPenalty += 10;
                }
            }

            if (d0 > 1024.0D) {
                this.delayCounter += 10;
            } else if (d0 > 256.0D) {
                this.delayCounter += 5;
            }

            if (!this.chicken.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.attackEntity(entitylivingbase, d0);
    }

    protected void attackEntity(EntityLivingBase attacked, double distance) {
        double d0 = this.getAttackReachSqr(attacked);

        if (distance <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.chicken.swingArm(EnumHand.MAIN_HAND);
            this.chicken.attackEntityAsMob(attacked);

            chicken.attackStateMachine = 2;
        }
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return (double) (this.chicken.width * 2.0F * this.chicken.width * 2.0F + attackTarget.width);
    }
}
