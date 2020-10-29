package wayoftime.bloodmagic.common.registration.impl;

import javax.annotation.Nonnull;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;

public class IRecipeSerializerRegistryObject<RECIPE extends IRecipe<?>>
		extends WrappedRegistryObject<IRecipeSerializer<RECIPE>>
{

	public IRecipeSerializerRegistryObject(RegistryObject<IRecipeSerializer<RECIPE>> registryObject)
	{
		super(registryObject);
	}

	@Nonnull
	public IRecipeSerializer<RECIPE> getRecipeSerializer()
	{
		return get();
	}
}