package wayoftime.bloodmagic.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;

public class PotionSuspended extends PotionBloodMagic
{
	public static List<LivingEntity> noGravityList = new ArrayList<>();

	public PotionSuspended()
	{
		super(MobEffectCategory.NEUTRAL, 0x23DDE1);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if (!noGravityList.contains(entity))
		{
			noGravityList.add(entity);
			entity.setNoGravity(true);
		} else if (entity.getEffect(this).getDuration() <= 1)
		{
			noGravityList.remove(entity);
			entity.setNoGravity(false);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}