package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;

public class PotionPlantLeech extends PotionBloodMagic
{
	public PotionPlantLeech()
	{
		super(MobEffectCategory.HARMFUL, 0x00FF00FF);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		BMPotionUtils.damageMobAndGrowSurroundingPlants(entity, 2 + amplifier, 1, 0.5 * 3 / (amplifier + 3), 25 * (1 + amplifier));
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration % 10 == 0;
	}
}