package wayoftime.bloodmagic.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class NearestAttackableDummyGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T>
{
	public NearestAttackableDummyGoal(Mob goalOwnerIn, Class<T> targetClassIn, boolean checkSight)
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
		return this.mob.hasEffect(BloodMagicPotions.PASSIVITY.get());
	}

	@Override
	public boolean canContinueToUse()
	{
		return this.mob.hasEffect(BloodMagicPotions.PASSIVITY.get());
	}
}
