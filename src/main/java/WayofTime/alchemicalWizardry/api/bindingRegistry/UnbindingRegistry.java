package WayofTime.alchemicalWizardry.api.bindingRegistry;

import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class UnbindingRegistry
{
    public static List<UnbindingRecipe> unbindingRecipes = new LinkedList();

    public static void addAllUnbindingRecipesFromBinding()
    {
        for (BindingRecipe bindingRecipe : BindingRegistry.bindingRecipes)
        {
            unbindingRecipes.add(new UnbindingRecipe(bindingRecipe.requiredItem, bindingRecipe.outputItem));
        }
    }

    public static void registerRecipe(ItemStack output, ItemStack input)
    {
        unbindingRecipes.add(new UnbindingRecipe(output, input));
    }

    public static boolean isRequiredItemValid(ItemStack testItem)
    {
        for (UnbindingRecipe recipe : unbindingRecipes)
        {
            if (recipe.doesRequiredItemMatch(testItem))
            {
                return true;
            }
        }

        return false;
    }

    public static int getIndexForItem(ItemStack testItem)
    {
        int i = 0;
        for (UnbindingRecipe recipe : unbindingRecipes)
        {
            if (recipe.doesRequiredItemMatch(testItem))
            {
                return i;
            }
            i++;
        }

        return -1;
    }

    public static ItemStack getOutputForIndex(int index)
    {
        if (unbindingRecipes.size() <= index)
        {
            return null;
        }

        return unbindingRecipes.get(index).getResult();
    }
}
