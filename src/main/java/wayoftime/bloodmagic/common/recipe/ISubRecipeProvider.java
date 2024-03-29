package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;

/**
 * Interface for helping split the recipe provider over multiple classes to make
 * it a bit easier to interact with
 */
public interface ISubRecipeProvider
{
	void addRecipes(Consumer<FinishedRecipe> consumer);
}