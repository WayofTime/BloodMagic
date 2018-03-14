package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.recipe.LivingArmourDowngradeRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LivingArmourDowngradeRecipeRegistry {
    private static List<LivingArmourDowngradeRecipe> recipeList = new ArrayList<>();
    private static Map<ItemStack, Map<Integer, List<ITextComponent>>> dialogueMap = new HashMap<>();

    public static void registerRecipe(LivingArmourDowngradeRecipe recipe) {
        recipeList.add(recipe);
    }

    public static void registerDialog(ItemStack keyStack, Map<Integer, List<ITextComponent>> map) {
        dialogueMap.put(keyStack, map);
    }

    public static List<ITextComponent> getDialogForProcessTick(ItemStack keyStack, int tick) {
        for (Entry<ItemStack, Map<Integer, List<ITextComponent>>> entry : dialogueMap.entrySet()) {
            ItemStack key = entry.getKey();
            if (OreDictionary.itemMatches(key, keyStack, false)) {
                Map<Integer, List<ITextComponent>> map = entry.getValue();
                if (map.containsKey(tick)) {
                    return map.get(tick);
                }
            }
        }

        return null;
    }

    public static void registerRecipe(LivingArmourUpgrade upgrade, ItemStack keyStack, Object... recipe) {
        registerRecipe(new LivingArmourDowngradeRecipe(upgrade, keyStack, recipe));
    }

    public static LivingArmourDowngradeRecipe getMatchingRecipe(ItemStack keyStack, List<ItemStack> itemList, World world, BlockPos pos) {
        for (LivingArmourDowngradeRecipe recipe : recipeList) {
            if (recipe.matches(keyStack, itemList, world, pos)) {
                return recipe;
            }
        }

        return null;
    }

    public static List<LivingArmourDowngradeRecipe> getRecipeList() {
        return new ArrayList<>(recipeList);
    }
}