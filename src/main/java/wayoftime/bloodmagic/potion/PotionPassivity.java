package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.effect.MobEffectCategory;
import wayoftime.bloodmagic.entity.goal.NearestAttackableDummyGoal;

public class PotionPassivity extends PotionBloodMagic
{
	public PotionPassivity()
	{
		super(MobEffectCategory.HARMFUL, 0xFFFFFF);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if (!(entity instanceof PathfinderMob))
		{
			return;
		}

		PathfinderMob animal = (PathfinderMob) entity;

		TargetGoal goal = new NearestAttackableDummyGoal<>(animal, Monster.class, false);

		animal.targetSelector.addGoal(0, goal);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}