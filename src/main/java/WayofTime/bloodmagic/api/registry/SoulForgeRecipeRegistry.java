package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.recipe.SoulForgeRecipe;

public class SoulForgeRecipeRegistry
{
    public static List<SoulForgeRecipe> recipeList = new ArrayList<SoulForgeRecipe>();

    public static void registerRecipe(SoulForgeRecipe recipe)
    {
        recipeList.add(recipe);
    }

    public static void registerRecipe(ItemStack outputStack, double minimulSouls, double drain, Object... objects)
    {
        registerRecipe(new SoulForgeRecipe(outputStack, minimulSouls, drain, objects));
    }

    public static SoulForgeRecipe getMatchingRecipe(List<ItemStack> itemList, World world, BlockPos pos)
    {
        for (SoulForgeRecipe recipe : recipeList)
        {
            if (recipe.matches(itemList, world, pos))
            {
                return recipe;
            }
        }

        return null;
    }
}