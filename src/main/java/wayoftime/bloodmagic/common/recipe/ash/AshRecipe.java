package wayoftime.bloodmagic.common.recipe.ash;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

public interface AshRecipe extends Recipe<AshInput> {
    String NAME = "arcane_ash";

    @Override
    default RecipeType<?> getType() {
        return BMRecipes.ASH_TYPE.get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return false;
    }
}
