package wayoftime.bloodmagic.entity.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class SacrificialLambMeleeAttackGoal extends MeleeAttackGoal
{
	public SacrificialLambMeleeAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory)
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
		return this.mob.hasEffect(BloodMagicPotions.SACRIFICIAL_LAMB.get()) && super.canUse();
	}

	@Override
	public boolean canContinueToUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.SACRIFICIAL_LAMB.get()) && super.canContinueToUse();
	}
}
