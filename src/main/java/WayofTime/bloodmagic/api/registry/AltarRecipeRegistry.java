package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
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
            BloodMagicAPI.getLogger().error("Error adding recipe for " + recipe.input.getDisplayName() + (recipe.output == null ? "" : " -> " + recipe.output.getDisplayName()) + ". Recipe already exists.");
    }

    public static AltarRecipe getRecipeForInput(ItemStack input) {
        for (ItemStack stack : recipes.keySet())
            if (stack.getIsItemStackEqual(input))
                return recipes.get(stack);

        return null;
    }
}
