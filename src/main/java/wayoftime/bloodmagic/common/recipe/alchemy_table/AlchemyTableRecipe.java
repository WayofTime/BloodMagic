package wayoftime.bloodmagic.common.recipe.alchemy_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

import java.util.ArrayList;
import java.util.List;

public record AlchemyTableRecipe(List<Ingredient> inputs, int tier, int essence, int duration, ItemStack output) implements Recipe<AlchemyTableInput> {

    public static final String RECIPE_TYPE_NAME = "alchemy_table";

    @Override
    public boolean matches(AlchemyTableInput input, Level level) {
        // tier/essence is managed in AlchemyTableTile so the flags can be set correctly
        if (input.size() != inputs.size()) {
            return false;
        }

        List<Ingredient> ingredientList = new ArrayList<>(inputs);
        for (int i = 0; i < input.size(); i++) {
            boolean matched = false;
            for (int j = 0; j < ingredientList.size(); j++) {
                if (ingredientList.get(j).test(input.getItem(i))) {
                    matched = true;
                    ingredientList.remove(j);
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(AlchemyTableInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ALCHEMY_TABLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.ALCHEMY_TABLE_TYPE.get();
    }
}
