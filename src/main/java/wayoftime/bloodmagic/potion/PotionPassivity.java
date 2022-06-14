package wayoftime.bloodmagic.potion;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectType;
import wayoftime.bloodmagic.entity.goal.NearestAttackableDummyGoal;

public class PotionPassivity extends PotionBloodMagic
{
	public PotionPassivity()
	{
		super(EffectType.HARMFUL, 0xFFFFFF);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if (!(entity instanceof CreatureEntity))
		{
			return;
		}

		CreatureEntity animal = (CreatureEntity) entity;

		TargetGoal goal = new NearestAttackableDummyGoal<>(animal, MonsterEntity.class, false);

		animal.targetSelector.addGoal(0, goal);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}