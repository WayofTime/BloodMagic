package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;

public class IRecipeSerializerDeferredRegister extends WrappedDeferredRegister<RecipeSerializer<?>>
{

	public IRecipeSerializerDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.Keys.RECIPE_SERIALIZERS);
	}

	public <RECIPE extends Recipe<?>> IRecipeSerializerRegistryObject<RECIPE> register(String name, Supplier<RecipeSerializer<RECIPE>> sup)
	{
		return register(name, sup, IRecipeSerializerRegistryObject::new);
	}
}
