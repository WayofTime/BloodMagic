package wayoftime.bloodmagic.common.recipe.ash;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

public record AshCraftingRecipe(Ingredient first, Ingredient second, ItemStack output) implements AshRecipe {
    @Override
    public boolean matches(AshInput input, Level level) {
        return first.test(input.getItem(0)) && second.test(input.getItem(1));
    }

    @Override
    public ItemStack assemble(AshInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ASH_CRAFTING_SERIALIZER.get();
    }
}
