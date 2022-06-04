package wayoftime.bloodmagic.potion;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Explosion.Mode;
import wayoftime.bloodmagic.entity.goal.SacrificialLambMeleeAttackGoal;

public class PotionSacrificialLamb extends PotionBloodMagic
{
	public PotionSacrificialLamb()
	{
		super(EffectType.HARMFUL, 0xFFFFFF);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if (!(entity instanceof CreatureEntity))
		{
			return;
		}

		CreatureEntity animal = (CreatureEntity) entity;

		TargetGoal goal = new NearestAttackableTargetGoal<>(animal, MonsterEntity.class, false);
		MeleeAttackGoal attackGoal = new SacrificialLambMeleeAttackGoal(animal, 2.0D, false);

		animal.targetSelector.addGoal(2, goal);
		animal.goalSelector.addGoal(2, attackGoal);

		if (animal.getAttackTarget() != null && animal.getDistanceSq(animal.getAttackTarget()) < 4)
		{
			animal.getEntityWorld().createExplosion(null, animal.getPosX(), animal.getPosY() + (double) (animal.getHeight() / 16.0F), animal.getPosZ(), 2 + animal.getActivePotionEffect(BloodMagicPotions.SACRIFICIAL_LAMB).getAmplifier() * 1.5f, false, Mode.NONE);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}