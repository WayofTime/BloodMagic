package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class AltarRecipeRegistry {

    @Getter
    private static BiMap<ItemStack, AltarRecipe> recipes = HashBiMap.create();

    public static void registerRecipe(AltarRecipe recipe) {
        if (!recipes.containsValue(recipe))
            recipes.put(recipe.input, recipe);
        else
            BloodMagicAPI.getLogger().error("Error adding recipe for " + recipe.input.getDisplayName() + (recipe.output == null ? "" : " -> " + recipe.output.getDisplayName()) + ". Recipe already exists.");
    }

    public static void registerRecipe(ItemStack input, @Nullable ItemStack output, int minTier, int syphon, int consumeRate, int drainRate, boolean useTag) {
        AltarRecipe recipe = new AltarRecipe(input, output, EnumAltarTier.values()[minTier], syphon, consumeRate, drainRate, useTag);
        registerRecipe(recipe);
    }

    public static AltarRecipe getRecipeForInput(ItemStack input) {
        return recipes.get(input);
    }
}
