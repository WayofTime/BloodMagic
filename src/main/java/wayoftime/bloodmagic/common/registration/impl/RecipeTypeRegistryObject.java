package wayoftime.bloodmagic.common.registration.impl;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;
import wayoftime.bloodmagic.recipe.BloodMagicRecipe;

public class RecipeTypeRegistryObject<RECIPE extends BloodMagicRecipe> extends WrappedRegistryObject<RecipeType<RECIPE>>
{

	public RecipeTypeRegistryObject(RegistryObject<RecipeType<RECIPE>> registryObject)
	{
		super(registryObject);
	}

//	@Nonnull
//	@Override
//	public EntityType<ENTITY> getEntityType()
//	{
//		return get();
//	}
}
