package wayoftime.bloodmagic.util.providers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import wayoftime.bloodmagic.util.text.IHasTextComponent;
import wayoftime.bloodmagic.util.text.IHasTranslationKey;

public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey
{
	ResourceLocation getRegistryName();

	default String getName()
	{
		return getRegistryName().getPath();
	}

	@Override
	default Component getTextComponent()
	{
		return new TranslatableComponent(getTranslationKey());
	}
}