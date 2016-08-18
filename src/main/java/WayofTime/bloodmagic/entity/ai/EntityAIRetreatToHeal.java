package WayofTime.bloodmagic.entity.ai;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class EntityAIRetreatToHeal<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    /** The entity we are attached to */
    protected EntitySentientSpecter theEntity;
    private double farSpeed;
    private double nearSpeed;
    private double safeHealDistance = 3;
    protected T closestLivingEntity;
    private float avoidDistance;
    /** The PathEntity of our entity */
    private Path entityPathEntity;
    /** The PathNavigate of our entity */
    private PathNavigate entityPathNavigate;
    private Class<T> classToAvoid;
    private Predicate<? super T> avoidTargetSelector;

    public EntityAIRetreatToHeal(EntitySentientSpecter theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this(theEntityIn, classToAvoidIn, Predicates.<T>alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityAIRetreatToHeal(EntitySentientSpecter theEntityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this.canBeSeenSelector = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_.isEntityAlive() && EntityAIRetreatToHeal.this.theEntity.getEntitySenses().canSee(p_apply_1_);
            }
        };
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
    public boolean shouldExecute()
    {
        if (!this.theEntity.shouldSelfHeal())
        {
            return false;
        }

        //This part almost doesn't matter
        List<T> list = this.theEntity.worldObj.<T>getEntitiesWithinAABB(this.classToAvoid, this.theEntity.getEntityBoundingBox().expand((double) this.avoidDistance, 3.0D, (double) this.avoidDistance), Predicates.and(new Predicate[] { EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector }));

        if (list.isEmpty())
        {
            return true; //No entities nearby, so I can freely heal
        } else
        {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null)
            {
                return false; //Nowhere to run, gotta fight!
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity))
            {
                return false; //I'll be headed off if I choose this direction.
            } else
            {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3d);
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return this.theEntity.shouldSelfHeal();//!this.entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        if (this.entityPathEntity != null)
        {
            this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
        }
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        if (this.closestLivingEntity != null)
        {
            if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D)
            {
                this.theEntity.getNavigator().setSpeed(this.nearSpeed);
            } else
            {
                this.theEntity.getNavigator().setSpeed(this.farSpeed);
            }

            if (this.theEntity.ticksExisted % 20 == 0 && this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) >= safeHealDistance * safeHealDistance)
            {
                healEntity();
                return;
            }
        }

        if (this.theEntity.ticksExisted % 20 == 0)
        {
            healEntity();
        }
    }

    public void healEntity()
    {
        this.theEntity.performEmergencyHeal(2);
    }
}