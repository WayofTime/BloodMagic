package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.item.ItemStack;

public class BindingRecipeRegistry {

    @Getter
    private static BiMap<ItemStack, BindingRecipe> recipes = HashBiMap.create();

    public static void registerRecipe(BindingRecipe recipe) {
        if (!recipes.containsValue(recipe))
            recipes.put(recipe.getInput(), recipe);
        else
            BloodMagicAPI.getLogger().error("Error adding binding recipe for %s %s. Recipe already exists.", recipe.input.getDisplayName(), recipe.output == null ? "" : " -> " + recipe.output.getDisplayName());
    }

    public static BindingRecipe getRecipeForInput(ItemStack input) {
        return recipes.get(input);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class BindingRecipe {

        private final ItemStack input;
        private final ItemStack output;
    }
}
