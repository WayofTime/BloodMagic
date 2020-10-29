package wayoftime.bloodmagic.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class IRecipeAlchemyArray extends RecipeAlchemyArray
{
	public IRecipeAlchemyArray(ResourceLocation id, ResourceLocation texture, Ingredient baseIngredient, Ingredient addedIngredient, ItemStack result)
	{
		super(id, texture, baseIngredient, addedIngredient, result);
	}

	@Override
	public IRecipeSerializer<RecipeAlchemyArray> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARRAY.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeAlchemyArray> getType()
	{
		return BloodMagicRecipeType.ARRAY;
	}

}
