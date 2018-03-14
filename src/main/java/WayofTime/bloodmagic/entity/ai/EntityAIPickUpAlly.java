package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityAspectedDemonBase;
import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityAIPickUpAlly extends EntityAIBase {
    protected final int attackInterval = 20;
    protected EntityAspectedDemonBase entity;
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

    private EntityLivingBase pickupTarget = null;

    public EntityAIPickUpAlly(EntityAspectedDemonBase creature, double speedIn, boolean useLongMemory) {
        this.entity = creature;
        this.worldObj = creature.getEntityWorld();
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.entity.getRidingEntity() != null) {
            return false;
        }

        AxisAlignedBB bb = new AxisAlignedBB(entity.posX - 0.5, entity.posY - 0.5, entity.posZ - 0.5, entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5).grow(5);
        List<EntityLivingBase> list = this.entity.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, bb, new EntityAspectedDemonBase.WillTypePredicate(entity.getType()));
        for (EntityLivingBase testEntity : list) {
            if (testEntity != this.entity) {
                Path path = this.entity.getNavigator().getPathToEntityLiving(testEntity);
                if (path != null) {
                    this.entityPathEntity = path;
                    this.pickupTarget = testEntity;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.entity.getRidingEntity() != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.entity.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.entity.getNavigator().clearPath();
        this.pickupTarget = null;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.pickupTarget;
        this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        --this.delayCounter;

        if ((this.longMemory || this.entity.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.entity.getRNG().nextFloat() < 0.05F)) {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.entity.getRNG().nextInt(7);

            if (this.canPenalize) {
                this.delayCounter += failedPathFindingPenalty;
                if (this.entity.getNavigator().getPath() != null) {
                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.entity.getNavigator().getPath().getFinalPathPoint();
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

            if (!this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.pickUpEntity(entitylivingbase, d0);
    }

    protected void pickUpEntity(EntityLivingBase potentialPickup, double distance) {
        double d0 = this.getAttackReachSqr(potentialPickup);

        if (distance <= d0 && this.attackTick <= 0 && !potentialPickup.isRiding()) {
            BMLog.DEBUG.info("Hai!");
            potentialPickup.startRiding(this.entity, true);
        }
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return (double) (this.entity.width * 2.0F * this.entity.width * 2.0F + attackTarget.width);
    }
}