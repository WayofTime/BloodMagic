package wayoftime.bloodmagic.potion;

import java.util.Random;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Explosion;

public class PotionFireFuse extends PotionBloodMagic
{
	public PotionFireFuse()
	{
		super(EffectType.HARMFUL, 0xFF0000FF);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if (entity.world.isRemote)
		{
			return;
		}

		Random random = entity.world.rand;
		entity.getEntityWorld().addParticle(ParticleTypes.FLAME, entity.getPosX()
				+ random.nextDouble() * 0.3, entity.getPosY()
						+ random.nextDouble() * 0.3, entity.getPosZ() + random.nextDouble() * 0.3, 0, 0.06d, 0);

		int radius = amplifier + 1;

		if (entity.getActivePotionEffect(BloodMagicPotions.FIRE_FUSE).getDuration() <= 3)
		{
			Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entity.world, entity)
					? Explosion.Mode.DESTROY
					: Explosion.Mode.NONE;
			entity.getEntityWorld().createExplosion(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), radius, false, explosion$mode);
		}
	}
}