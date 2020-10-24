package wayoftime.bloodmagic.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class DamageSourceBloodMagic extends DamageSource
{
	public static final DamageSourceBloodMagic INSTANCE = new DamageSourceBloodMagic();

	public DamageSourceBloodMagic()
	{
		super("bloodMagic");

		setDamageBypassesArmor();
		setDamageIsAbsolute();
	}

	@Override
	public ITextComponent getDeathMessage(LivingEntity livingBase)
	{
		return new StringTextComponent(TextHelper.localizeEffect("chat.bloodmagic.damageSource", livingBase.getName()));
	}
}