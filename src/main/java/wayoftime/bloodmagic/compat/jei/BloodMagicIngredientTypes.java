package wayoftime.bloodmagic.compat.jei;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.effect.MobEffect;

public class BloodMagicIngredientTypes
{
	public static final IIngredientType<MobEffect> EFFECT = () -> MobEffect.class;

	private BloodMagicIngredientTypes()
	{

	}
}
