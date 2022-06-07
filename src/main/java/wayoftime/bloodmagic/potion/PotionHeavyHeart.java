package wayoftime.bloodmagic.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;

public class PotionHeavyHeart extends PotionBloodMagic
{
	public PotionHeavyHeart()
	{
		super(EffectType.HARMFUL, 0x000000);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		double modifier = -0.03 * (amplifier + 1);
		Vector3d motion = entity.getMotion();
		motion = motion.add(0, modifier, 0);
		entity.setMotion(motion);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}