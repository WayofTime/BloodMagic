package wayoftime.bloodmagic.util.providers;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;

public interface IEntityTypeProvider extends IBaseProvider
{

	@Nonnull
	EntityType<?> getEntityType();

	@Override
	default ResourceLocation getRegistryName()
	{
		return ForgeRegistries.ENTITY_TYPES.getKey(getEntityType());
	}

	@Override
	default Component getTextComponent()
	{
		return getEntityType().getDescription();
	}

	@Override
	default String getTranslationKey()
	{
		return getEntityType().getDescriptionId();
	}
}