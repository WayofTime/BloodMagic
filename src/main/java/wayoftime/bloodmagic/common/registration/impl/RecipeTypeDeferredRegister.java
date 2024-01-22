package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;
import wayoftime.bloodmagic.recipe.BloodMagicRecipe;

public class RecipeTypeDeferredRegister extends WrappedDeferredRegister<RecipeType<?>>
{
	public RecipeTypeDeferredRegister(String modid)
	{
		super(modid, Registries.RECIPE_TYPE);
	}

	public <RECIPE extends BloodMagicRecipe> RecipeTypeRegistryObject<RECIPE> register(String name, Supplier<? extends RecipeType<RECIPE>> sup)
	{
		return register(name, sup, RecipeTypeRegistryObject::new);
	}
}