package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.phys.Vec3;

public class PotionHeavyHeart extends PotionBloodMagic
{
	public PotionHeavyHeart()
	{
		super(MobEffectCategory.HARMFUL, 0x000000);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		double modifier = -0.03 * (amplifier + 1);
		Vec3 motion = entity.getDeltaMovement();
		motion = motion.add(0, modifier, 0);
		entity.setDeltaMovement(motion);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}