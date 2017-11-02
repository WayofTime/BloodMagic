package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityAspectedDemonBase;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityAIProtectAlly extends EntityAIBase {
    /**
     * The entity owner of this AITask
     */
    private final EntityCorruptedSheep entity;
    /**
     * The world the grass eater entity is eating from
     */
    private final World world;
    /**
     * Number of ticks since the entity started to eat grass
     */
    int castTimer;

    public EntityAIProtectAlly(EntityCorruptedSheep entity) {
        this.entity = entity;
        this.world = entity.getEntityWorld();
        this.setMutexBits(7);
    }

    public int getCastTimer() {
        return this.castTimer;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        AxisAlignedBB bb = new AxisAlignedBB(entity.posX - 0.5, entity.posY - 0.5, entity.posZ - 0.5, entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5).grow(5);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, bb, new EntityAspectedDemonBase.WillTypePredicate(entity.getType()));
        for (EntityLivingBase testEntity : list) {
            if (testEntity != this.entity) {
                if (this.entity.canProtectAlly(testEntity)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.castTimer = 100;
        this.world.setEntityState(this.entity, (byte) 53);
        this.entity.getNavigator().clearPath();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.castTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return castTimer > 0;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        this.castTimer = Math.max(0, this.castTimer - 1);
        if (castTimer == 0) {
            AxisAlignedBB bb = new AxisAlignedBB(entity.posX - 0.5, entity.posY - 0.5, entity.posZ - 0.5, entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5).grow(5);
            List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, bb, new EntityAspectedDemonBase.WillTypePredicate(entity.getType()));
            for (EntityLivingBase testEntity : list) {
                if (testEntity != this.entity) {
                    if (this.entity.applyProtectionToAlly(testEntity)) {
                        return;
                    }
                }
            }
        }
    }
}