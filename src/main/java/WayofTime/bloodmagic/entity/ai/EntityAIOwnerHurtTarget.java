package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntitySentientSpecter theEntitySentientSpecter;
    EntityLivingBase theTarget;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntitySentientSpecter theEntitySentientSpecterIn)
    {
        super(theEntitySentientSpecterIn, false);
        this.theEntitySentientSpecter = theEntitySentientSpecterIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theEntitySentientSpecter.isTamed())
        {
            return false;
        } else
        {
            EntityLivingBase entitylivingbase = this.theEntitySentientSpecter.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            } else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.timestamp && this.isSuitableTarget(this.theTarget, false) && this.theEntitySentientSpecter.shouldAttackEntity(this.theTarget, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entitylivingbase = this.theEntitySentientSpecter.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getLastAttackerTime();
        }

        super.startExecuting();
    }
}