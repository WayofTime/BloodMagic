package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityDemonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
    EntityDemonBase theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
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
            EntityLivingBase owner = this.theDefendingTameable.getOwner();

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
        EntityLivingBase owner = this.theDefendingTameable.getOwner();

        if (owner != null) {
            this.timestamp = owner.getRevengeTimer();
        }

        super.startExecuting();
    }
}