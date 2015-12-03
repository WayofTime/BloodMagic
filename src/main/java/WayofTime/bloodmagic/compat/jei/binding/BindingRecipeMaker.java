package WayofTime.bloodmagic.compat.jei.binding;

import WayofTime.bloodmagic.api.registry.BindingRecipeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BindingRecipeMaker {

    @Nonnull
    public static List<BindingRecipeJEI> getRecipes() {
        Map<ItemStack, BindingRecipeRegistry.BindingRecipe> altarMap = BindingRecipeRegistry.getRecipes();

        ArrayList<BindingRecipeJEI> recipes = new ArrayList<BindingRecipeJEI>();

        for (Map.Entry<ItemStack, BindingRecipeRegistry.BindingRecipe> itemStackBindingRecipeEntry : altarMap.entrySet()) {
            ItemStack input = itemStackBindingRecipeEntry.getKey();
            ItemStack output = itemStackBindingRecipeEntry.getValue().getOutput();

            BindingRecipeJEI recipe = new BindingRecipeJEI(input, output);
            recipes.add(recipe);
        }

        return recipes;
    }
}
