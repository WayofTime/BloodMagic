package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIStealthTowardsTarget extends Goal {
    private final EntityCorruptedChicken entity;
    private final double speed;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private int ticksLeft = 0;

    public EntityAIStealthTowardsTarget(EntityCorruptedChicken creatureIn, double speedIn) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.attackStateMachine != 0 || this.entity.getAttackTarget() == null) {
            return false;
        }

        LivingEntity target = this.entity.getAttackTarget();
        Vec3d vec3d = null;
        if (target instanceof CreatureEntity) {
            vec3d = RandomPositionGenerator.findRandomTarget((CreatureEntity) target, 10, 7);
        } else {
            vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
        }

        if (vec3d == null) {
            return false;
        } else {
            ticksLeft = this.entity.getEntityWorld().rand.nextInt(200) + 100;
            this.xPosition = vec3d.x;
            this.yPosition = vec3d.y;
            this.zPosition = vec3d.z;
            return true;
        }
    }

    @Override
    public void resetTask() {
        ticksLeft = 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        ticksLeft--;
        if (ticksLeft <= 0) {
            this.entity.attackStateMachine = 1;
        }

        this.entity.cloak();

        if (this.entity.getNavigator().noPath()) {
            LivingEntity target = this.entity.getAttackTarget();
            Vec3d vec3d;
            if (target instanceof CreatureEntity) {
                vec3d = RandomPositionGenerator.findRandomTarget((CreatureEntity) target, 10, 7);
            } else {
                vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
            }

            if (vec3d != null) {
                this.xPosition = vec3d.x;
                this.yPosition = vec3d.y;
                this.zPosition = vec3d.z;
                this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
            }
        }

        return this.entity.attackStateMachine == 0;
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}