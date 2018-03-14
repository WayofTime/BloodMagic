package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipeRegistry {
    private static List<AlchemyTableRecipe> recipeList = new ArrayList<>();

    public static void registerRecipe(AlchemyTableRecipe recipe) {
        recipeList.add(recipe);
    }

    public static void registerRecipe(ItemStack outputStack, int lpDrained, int ticksRequired, int tierRequired, Object... objects) {
        registerRecipe(new AlchemyTableRecipe(outputStack, lpDrained, ticksRequired, tierRequired, objects));
    }

    public static void removeRecipe(AlchemyTableRecipe recipe) {
        recipeList.remove(recipe);
    }

    public static AlchemyTableRecipe getMatchingRecipe(List<ItemStack> itemList, World world, BlockPos pos) {
        for (AlchemyTableRecipe recipe : recipeList) {
            if (recipe.matches(itemList, world, pos)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<AlchemyTableRecipe> getRecipeList() {
        return new ArrayList<>(recipeList);
    }
}