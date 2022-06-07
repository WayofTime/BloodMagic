package wayoftime.bloodmagic.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class PotionPlantLeech extends PotionBloodMagic
{
	public PotionPlantLeech()
	{
		super(EffectType.HARMFUL, 0x00FF00FF);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		BMPotionUtils.damageMobAndGrowSurroundingPlants(entity, 2 + amplifier, 1, 0.5 * 3 / (amplifier + 3), 25 * (1 + amplifier));
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return duration % 10 == 0;
	}
}