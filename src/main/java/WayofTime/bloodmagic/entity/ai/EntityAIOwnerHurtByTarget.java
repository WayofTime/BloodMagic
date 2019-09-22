package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityDemonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

public class EntityAIOwnerHurtByTarget extends TargetGoal {
    EntityDemonBase theDefendingTameable;
    LivingEntity theOwnerAttacker;
    private int timestamp;

    public EntityAIOwnerHurtByTarget(EntityDemonBase theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.theDefendingTameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        } else {
            LivingEntity owner = this.theDefendingTameable.getOwner();

            if (owner == null) {
                return false;
            } else {
                this.theOwnerAttacker = owner.getRevengeTarget();
                int i = owner.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, owner);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        LivingEntity owner = this.theDefendingTameable.getOwner();

        if (owner != null) {
            this.timestamp = owner.getRevengeTimer();
        }

        super.startExecuting();
    }
}