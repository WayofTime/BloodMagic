package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;

public class TartaricForgeRecipeRegistry
{
    private static List<TartaricForgeRecipe> recipeList = new ArrayList<TartaricForgeRecipe>();

    public static void registerRecipe(TartaricForgeRecipe recipe)
    {
        recipeList.add(recipe);
    }

    public static void registerRecipe(ItemStack outputStack, double minimulSouls, double drain, Object... objects)
    {
        registerRecipe(new TartaricForgeRecipe(outputStack, minimulSouls, drain, objects));
    }

    public static TartaricForgeRecipe getMatchingRecipe(List<ItemStack> itemList, World world, BlockPos pos)
    {
        for (TartaricForgeRecipe recipe : recipeList)
        {
            if (recipe.matches(itemList, world, pos))
            {
                return recipe;
            }
        }

        return null;
    }

    public static List<TartaricForgeRecipe> getRecipeList()
    {
        return new ArrayList<TartaricForgeRecipe>(recipeList);
    }
}