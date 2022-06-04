package wayoftime.bloodmagic.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class SacrificialLambMeleeAttackGoal extends MeleeAttackGoal
{
	public SacrificialLambMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory)
	{
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
	{

	}

	@Override
	public boolean shouldExecute()
	{
		return this.attacker.isPotionActive(BloodMagicPotions.SACRIFICIAL_LAMB) && super.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting()
	{
		return this.attacker.isPotionActive(BloodMagicPotions.SACRIFICIAL_LAMB) && super.shouldContinueExecuting();
	}
}
