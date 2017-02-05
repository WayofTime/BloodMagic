package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;

public class EntityAIStealthTowardsTarget extends EntityAIBase
{
    private final EntityCorruptedChicken entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;

    private int ticksLeft = 0;

    public EntityAIStealthTowardsTarget(EntityCorruptedChicken creatureIn, double speedIn)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.entity.attackStateMachine != 0 || this.entity.getAttackTarget() == null)
        {
            return false;
        }

        EntityLivingBase target = this.entity.getAttackTarget();
        Vec3d vec3d = null;
        if (target instanceof EntityCreature)
        {
            vec3d = RandomPositionGenerator.findRandomTarget((EntityCreature) target, 10, 7);
        } else
        {
            vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
        }

        if (vec3d == null)
        {
            return false;
        } else
        {
            ticksLeft = this.entity.getEntityWorld().rand.nextInt(200) + 100;
            this.xPosition = vec3d.xCoord;
            this.yPosition = vec3d.yCoord;
            this.zPosition = vec3d.zCoord;
            return true;
        }
    }

    @Override
    public void resetTask()
    {
        ticksLeft = 0;
    }

    @Override
    public boolean continueExecuting()
    {
        ticksLeft--;
        if (ticksLeft <= 0)
        {
            this.entity.attackStateMachine = 1;
        }

        this.entity.cloak();

        if (this.entity.getNavigator().noPath())
        {
            EntityLivingBase target = this.entity.getAttackTarget();
            Vec3d vec3d = null;
            if (target instanceof EntityCreature)
            {
                vec3d = RandomPositionGenerator.findRandomTarget((EntityCreature) target, 10, 7);
            } else
            {
                vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
            }

            if (vec3d != null)
            {
                this.xPosition = vec3d.xCoord;
                this.yPosition = vec3d.yCoord;
                this.zPosition = vec3d.zCoord;
                this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
            }
        }

        return this.entity.attackStateMachine == 0;
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}