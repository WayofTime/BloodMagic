package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.entity.goal.SacrificialLambMeleeAttackGoal;

public class PotionSacrificialLamb extends PotionBloodMagic
{
	public PotionSacrificialLamb()
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

		TargetGoal goal = new NearestAttackableTargetGoal<>(animal, Monster.class, false);
		MeleeAttackGoal attackGoal = new SacrificialLambMeleeAttackGoal(animal, 2.0D, false);

		animal.targetSelector.addGoal(2, goal);
		animal.goalSelector.addGoal(2, attackGoal);

		if (animal.getTarget() != null && animal.distanceToSqr(animal.getTarget()) < 4)
		{
			animal.getCommandSenderWorld().explode(null, animal.getX(), animal.getY() + (double) (animal.getBbHeight() / 16.0F), animal.getZ(), 2 + animal.getEffect(BloodMagicPotions.SACRIFICIAL_LAMB.get()).getAmplifier() * 1.5f, false, Level.ExplosionInteraction.NONE);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}