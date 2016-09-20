package WayofTime.bloodmagic.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;

public class EntityAIAttackStealthMelee extends EntityAIAttackMelee
{
    protected EntityCorruptedChicken chicken;

    public EntityAIAttackStealthMelee(EntityCorruptedChicken creature, double speedIn, boolean useLongMemory)
    {
        super(creature, speedIn, useLongMemory);
        chicken = creature;
    }

    @Override
    public boolean shouldExecute()
    {
        return chicken.attackStateMachine == 1 && super.shouldExecute();
    }

    @Override
    public boolean continueExecuting()
    {
        return chicken.attackStateMachine == 1 && super.continueExecuting();
    }

    @Override
    public void resetTask()
    {
        if (chicken.attackStateMachine == 1)
        {
            chicken.attackStateMachine = 0;
        }
    }

    @Override
    protected void func_190102_a(EntityLivingBase attacked, double distance)
    {
        double d0 = this.getAttackReachSqr(attacked);

        if (distance <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(attacked);

            chicken.attackStateMachine = 2;
        }
    }
}
