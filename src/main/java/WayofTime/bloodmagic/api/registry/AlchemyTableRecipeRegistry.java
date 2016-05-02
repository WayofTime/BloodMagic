package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;

public class AlchemyTableRecipeRegistry
{
    private static List<AlchemyTableRecipe> recipeList = new ArrayList<AlchemyTableRecipe>();

    public static void registerRecipe(AlchemyTableRecipe recipe)
    {
        recipeList.add(recipe);
    }

    public static void registerRecipe(ItemStack outputStack, int lpDrained, int ticksRequired, int tierRequired, Object... objects)
    {
        registerRecipe(new AlchemyTableRecipe(outputStack, lpDrained, ticksRequired, tierRequired, objects));
    }

    public static AlchemyTableRecipe getMatchingRecipe(List<ItemStack> itemList, World world, BlockPos pos)
    {
        for (AlchemyTableRecipe recipe : recipeList)
        {
            if (recipe.matches(itemList, world, pos))
            {
                return recipe;
            }
        }

        return null;
    }

    public static List<AlchemyTableRecipe> getRecipeList()
    {
        return new ArrayList<AlchemyTableRecipe>(recipeList);
    }
}