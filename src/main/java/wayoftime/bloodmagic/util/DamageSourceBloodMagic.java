package wayoftime.bloodmagic.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
	public ITextComponent getLocalizedDeathMessage(LivingEntity livingBase)
	{
		return new TranslationTextComponent("chat.bloodmagic.damageSource", livingBase.getName());
	}
}