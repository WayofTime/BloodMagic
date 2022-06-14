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
	public void applyEffectTick(LivingEntity entity, int amplifier)
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

		if (animal.getTarget() != null && animal.distanceToSqr(animal.getTarget()) < 4)
		{
			animal.getCommandSenderWorld().explode(null, animal.getX(), animal.getY() + (double) (animal.getBbHeight() / 16.0F), animal.getZ(), 2 + animal.getEffect(BloodMagicPotions.SACRIFICIAL_LAMB).getAmplifier() * 1.5f, false, Mode.NONE);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}