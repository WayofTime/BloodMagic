package wayoftime.bloodmagic.api.providers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.api.text.IHasTextComponent;
import wayoftime.bloodmagic.api.text.IHasTranslationKey;

public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey
{
	ResourceLocation getRegistryName();

	default String getName()
	{
		return getRegistryName().getPath();
	}

	@Override
	default ITextComponent getTextComponent()
	{
		return new TranslationTextComponent(getTranslationKey());
	}
}