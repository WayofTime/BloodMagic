package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

import java.util.UUID;

public class EntityAIHurtByTargetIgnoreTamed extends HurtByTargetGoal {
    public EntityAIHurtByTargetIgnoreTamed(CreatureEntity creatureIn, boolean entityCallsForHelpIn, Class<?>... targetClassesIn) {
        super(creatureIn, true, targetClassesIn);
    }

    @Override
    public boolean isSuitableTarget(LivingEntity target, boolean includeInvincibles) {
        if (this.taskOwner instanceof IEntityOwnable && target instanceof IEntityOwnable) {
            UUID thisId = ((IEntityOwnable) this.taskOwner).getOwnerId();
            UUID targetId = ((IEntityOwnable) target).getOwnerId();
            if (thisId != null && targetId != null && thisId.equals(targetId)) {
                return false;
            }
        }

        return super.isSuitableTarget(target, includeInvincibles);
    }
}