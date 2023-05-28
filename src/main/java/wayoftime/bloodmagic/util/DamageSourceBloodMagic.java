package wayoftime.bloodmagic.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DamageSourceBloodMagic extends DamageSource
{
	public static final DamageSourceBloodMagic INSTANCE = new DamageSourceBloodMagic();

	public DamageSourceBloodMagic()
	{
		super("bloodMagic");

		bypassArmor();
		bypassMagic();
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity livingBase)
	{
		return Component.translatable("chat.bloodmagic.damageSource", livingBase.getName());
	}
}