package WayofTime.alchemicalWizardry.api.bindingRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnbindingRegistry
{
    public static List<UnbindingRecipe> unbindingRecipes = new LinkedList<UnbindingRecipe>();

    public static void addAllUnbindingRecipesFromBinding()
    {
        for (BindingRecipe bindingRecipe : BindingRegistry.bindingRecipes)
        {
            List<ItemStack> subList = new ArrayList<ItemStack>();
            subList.add(bindingRecipe.requiredItem);
            unbindingRecipes.add(new UnbindingRecipe(bindingRecipe.outputItem, subList));
        }
    }

    public static void registerRecipe(ItemStack input, List<ItemStack> output)
    {
        unbindingRecipes.add(new UnbindingRecipe(input, output));
    }

    public static void registerRecipe(Item input, List<ItemStack> output)
    {
        unbindingRecipes.add(new UnbindingRecipe(new ItemStack(input), output));
    }

    public static void registerRecipe(Block input, List<ItemStack> output)
    {
        unbindingRecipes.add(new UnbindingRecipe(new ItemStack(input), output));
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

    public static List<ItemStack> getOutputForIndex(int index)
    {
        if (unbindingRecipes.size() <= index)
        {
            return null;
        }

        return unbindingRecipes.get(index).getResult();
    }
}
