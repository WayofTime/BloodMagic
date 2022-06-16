package wayoftime.bloodmagic.common.registration.impl;

import javax.annotation.Nonnull;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;

public class IRecipeSerializerRegistryObject<RECIPE extends Recipe<?>> extends WrappedRegistryObject<RecipeSerializer<RECIPE>>
{

	public IRecipeSerializerRegistryObject(RegistryObject<RecipeSerializer<RECIPE>> registryObject)
	{
		super(registryObject);
	}

	@Nonnull
	public RecipeSerializer<RECIPE> getRecipeSerializer()
	{
		return get();
	}
}