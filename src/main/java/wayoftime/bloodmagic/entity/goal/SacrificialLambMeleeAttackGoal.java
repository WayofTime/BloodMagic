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
	public boolean canUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.SACRIFICIAL_LAMB) && super.canUse();
	}

	@Override
	public boolean canContinueToUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.SACRIFICIAL_LAMB) && super.canContinueToUse();
	}
}
