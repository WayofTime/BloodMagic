package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import WayofTime.bloodmagic.entity.mob.EntityDemonBase;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntityDemonBase theEntityDemonBase;
    EntityLivingBase theTarget;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntityDemonBase theEntityDemonBaseIn)
    {
        super(theEntityDemonBaseIn, false);
        this.theEntityDemonBase = theEntityDemonBaseIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theEntityDemonBase.isTamed())
        {
            return false;
        } else
        {
            EntityLivingBase entitylivingbase = this.theEntityDemonBase.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            } else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.timestamp && this.isSuitableTarget(this.theTarget, false) && this.theEntityDemonBase.shouldAttackEntity(this.theTarget, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entitylivingbase = this.theEntityDemonBase.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getLastAttackerTime();
        }

        super.startExecuting();
    }
}