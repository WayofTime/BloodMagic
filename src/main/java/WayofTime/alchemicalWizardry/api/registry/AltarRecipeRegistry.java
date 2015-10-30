package WayofTime.alchemicalWizardry.api.registry;

import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import WayofTime.alchemicalWizardry.api.altar.AltarRecipe;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import net.minecraft.item.ItemStack;

public class AltarRecipeRegistry {

    @Getter
    private static BiMap<ItemStack, AltarRecipe> recipes = HashBiMap.create();

    public static void registerRecipe(AltarRecipe recipe) {
        if (!recipes.containsValue(recipe))
            recipes.put(recipe.input, recipe);
        else
            AlchemicalWizardryAPI.getLogger().error("Error adding recipe for " + recipe.input.getDisplayName() + (recipe.output == null ? "" : " -> " + recipe.output.getDisplayName()) + ". Recipe already exists.");
    }

    public static AltarRecipe getRecipeForInput(ItemStack input) {
        return recipes.get(input);
    }
}
