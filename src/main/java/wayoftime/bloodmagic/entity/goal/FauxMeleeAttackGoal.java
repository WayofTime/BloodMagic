package wayoftime.bloodmagic.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class FauxMeleeAttackGoal extends MeleeAttackGoal
{

	public FauxMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory)
	{
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
	{

	}
}
