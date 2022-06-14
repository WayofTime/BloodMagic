package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;

public class PotionSoftFall extends PotionBloodMagic
{
	public PotionSoftFall()
	{
		super(MobEffectCategory.BENEFICIAL, 0x4AEDD9);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		entity.fallDistance = 0;
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}