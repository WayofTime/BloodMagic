package wayoftime.bloodmagic.util.providers;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
		return Component.translatable(getTranslationKey());
	}
}