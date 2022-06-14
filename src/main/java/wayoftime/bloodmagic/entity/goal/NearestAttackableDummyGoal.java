package wayoftime.bloodmagic.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class NearestAttackableDummyGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T>
{
	public NearestAttackableDummyGoal(MobEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight)
	{
		super(goalOwnerIn, targetClassIn, checkSight);
	}

	@Override
	public void start()
	{

	}

	@Override
	public boolean canUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.PASSIVITY);
	}

	@Override
	public boolean canContinueToUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.PASSIVITY);
	}
}
