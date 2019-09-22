package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityDemonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

public class EntityAIOwnerHurtTarget extends TargetGoal {
    EntityDemonBase theEntityDemonBase;
    LivingEntity theTarget;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntityDemonBase theEntityDemonBaseIn) {
        super(theEntityDemonBaseIn, false);
        this.theEntityDemonBase = theEntityDemonBaseIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.theEntityDemonBase.isTamed()) {
            return false;
        } else {
            LivingEntity entitylivingbase = this.theEntityDemonBase.getOwner();

            if (entitylivingbase == null) {
                return false;
            } else {
                this.theTarget = entitylivingbase.getLastAttackedEntity();
                int i = entitylivingbase.getLastAttackedEntityTime();
                return i != this.timestamp && this.isSuitableTarget(this.theTarget, false) && this.theEntityDemonBase.shouldAttackEntity(this.theTarget, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        LivingEntity entitylivingbase = this.theEntityDemonBase.getOwner();

        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}