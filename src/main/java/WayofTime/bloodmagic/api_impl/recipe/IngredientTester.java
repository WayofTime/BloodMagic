package WayofTime.bloodmagic.api_impl.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class IngredientTester {

    public static boolean compareIngredients(Ingredient ingredient1, Ingredient ingredient2) {
        ItemStack[] stacks1 = ingredient1.getMatchingStacks();
        ItemStack[] stacks2 = ingredient2.getMatchingStacks();

        if (stacks1.length != stacks2.length)
            return false;

        for (int i = 0; i < stacks1.length; i++)
            if (!ItemStack.areItemStacksEqualUsingNBTShareTag(stacks1[i], stacks2[i]))
                return false;

        return true;
    }

    public static boolean compareIngredients(List<Ingredient> ingredients1, List<Ingredient> ingredients2) {
        if (ingredients1.size() != ingredients2.size())
            return false;

        for (int i = 0; i < ingredients1.size(); i++)
            if (!compareIngredients(ingredients1.get(i), ingredients2.get(i)))
                return false;

        return true;
    }
}
