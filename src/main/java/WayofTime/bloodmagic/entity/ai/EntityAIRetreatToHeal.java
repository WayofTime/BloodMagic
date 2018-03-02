package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityDemonBase;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityAIRetreatToHeal<T extends Entity> extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector;
    /**
     * The entity we are attached to
     */
    protected EntityDemonBase theEntity;
    protected T closestLivingEntity;
    private double farSpeed;
    private double nearSpeed;
    private double safeHealDistance = 3;
    private float avoidDistance;
    /**
     * The PathEntity of our entity
     */
    private Path entityPathEntity;
    /**
     * The PathNavigate of our entity
     */
    private PathNavigate entityPathNavigate;
    private Class<T> classToAvoid;
    private Predicate<? super T> avoidTargetSelector;

    public EntityAIRetreatToHeal(EntityDemonBase theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(theEntityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityAIRetreatToHeal(EntityDemonBase theEntityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.canBeSeenSelector = p_apply_1_ -> p_apply_1_.isEntityAlive() && EntityAIRetreatToHeal.this.theEntity.getEntitySenses().canSee(p_apply_1_);
        this.theEntity = theEntityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (!this.theEntity.shouldEmergencyHeal()) {
            return false;
        }

        //This part almost doesn't matter
        List<T> list = this.theEntity.getEntityWorld().getEntitiesWithinAABB(this.classToAvoid, this.theEntity.getEntityBoundingBox().expand((double) this.avoidDistance, 3.0D, (double) this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));

        if (list.isEmpty()) {
            return true; //No entities nearby, so I can freely heal
        } else {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null) {
                return false; //Nowhere to run, gotta fight!
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.theEntity)) {
                return false; //I'll be headed off if I choose this direction.
            } else {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.entityPathEntity != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.theEntity.shouldEmergencyHeal();//!this.entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        if (this.entityPathEntity != null) {
            this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
        }
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if (this.closestLivingEntity != null) {
            if (this.theEntity.getDistanceSq(this.closestLivingEntity) < 49.0D) {
                this.theEntity.getNavigator().setSpeed(this.nearSpeed);
            } else {
                this.theEntity.getNavigator().setSpeed(this.farSpeed);
            }

            if (this.theEntity.ticksExisted % 20 == 0 && this.theEntity.getDistanceSq(this.closestLivingEntity) >= safeHealDistance * safeHealDistance) {
                healEntity();
                return;
            }
        }

        if (this.theEntity.ticksExisted % 20 == 0) {
            healEntity();
        }
    }

    public void healEntity() {
        this.theEntity.performEmergencyHeal(2);
    }
}