package wayoftime.bloodmagic.potion;

import java.util.Random;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class PotionFireFuse extends PotionBloodMagic
{
	public PotionFireFuse()
	{
		super(MobEffectCategory.HARMFUL, 0xFF0000FF);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if (entity.level().isClientSide)
		{
			return;
		}

		RandomSource random = entity.level().random;
		entity.getCommandSenderWorld().addParticle(ParticleTypes.FLAME, entity.getX() + random.nextDouble() * 0.3, entity.getY() + random.nextDouble() * 0.3, entity.getZ() + random.nextDouble() * 0.3, 0, 0.06d, 0);

		int radius = amplifier + 1;

		if (entity.getEffect(BloodMagicPotions.FIRE_FUSE.get()).getDuration() <= 3)
		{
			Level.ExplosionInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entity.level(), entity)
					? Level.ExplosionInteraction.TNT
					: Level.ExplosionInteraction.NONE;
			entity.getCommandSenderWorld().explode(null, entity.getX(), entity.getY(), entity.getZ(), radius, false, explosion$mode);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
}