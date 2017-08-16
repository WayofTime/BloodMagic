package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityMimic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMimicReform extends EntityAIBase {
    private final EntityMimic theEntity;

    public EntityAIMimicReform(EntityMimic creatureIn) {
        this.theEntity = creatureIn;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        return this.theEntity.ticksExisted > 100 && this.theEntity.hasHome() && this.theEntity.isWithinHomeDistanceCurrentPosition();
    }

    @Override
    public void startExecuting() {
        BlockPos homePos = this.theEntity.getHomePosition();
        if (theEntity.reformIntoMimicBlock(homePos)) {
            this.theEntity.setDead();
        }
    }
}