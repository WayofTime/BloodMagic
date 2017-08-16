package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

import java.util.UUID;

public class EntityAIHurtByTargetIgnoreTamed extends EntityAIHurtByTarget {
    public EntityAIHurtByTargetIgnoreTamed(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... targetClassesIn) {
        super(creatureIn, true, targetClassesIn);
    }

    @Override
    public boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
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