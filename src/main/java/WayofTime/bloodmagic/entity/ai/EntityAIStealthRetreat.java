package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.Vec3d;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;

public class EntityAIStealthRetreat extends EntityAIBase
{
    /** The entity we are attached to */
    protected EntityCorruptedChicken entity;
    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    /** The PathEntity of our entity */
    private Path entityPathEntity;
    /** The PathNavigate of our entity */
    private final PathNavigate entityPathNavigate;

    private int ticksLeft = 0;

    public EntityAIStealthRetreat(EntityCorruptedChicken theEntityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this.entity = theEntityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.entity.attackStateMachine == 2)
        {
            EntityLivingBase attacked = this.entity.getAttackTarget();
            if (attacked == null)
            {
                return false;
            }

            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(attacked.posX, attacked.posY, attacked.posZ));

            if (vec3d == null)
            {
                return false;
            } else if (attacked.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < attacked.getDistanceSqToEntity(this.entity))
            {
                return false;
            } else
            {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                return this.entityPathEntity != null;
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        if (this.entityPathNavigate.noPath())
        {
            this.entity.attackStateMachine = 0;
            return false;
        }

        return this.entity.attackStateMachine == 2;
    }

    @Override
    public void resetTask()
    {
        ticksLeft = 0;
    }

    @Override
    public void startExecuting()
    {
        ticksLeft = this.entity.getEntityWorld().rand.nextInt(100) + 100;
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    @Override
    public void updateTask()
    {
        ticksLeft--;
        if (ticksLeft <= 0 || this.entity.getAttackTarget() == null)
        {
            this.entity.attackStateMachine = 0;
            return;
        }

        if (this.entity.getDistanceSqToEntity(this.entity.getAttackTarget()) < 49.0D)
        {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        } else
        {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}