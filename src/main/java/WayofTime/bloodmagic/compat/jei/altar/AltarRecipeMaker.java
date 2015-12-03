package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AltarRecipeMaker {

    @Nonnull
    public static List<AltarRecipeJEI> getRecipes() {
        Map<ItemStack, AltarRecipeRegistry.AltarRecipe> altarMap = AltarRecipeRegistry.getRecipes();

        ArrayList<AltarRecipeJEI> recipes = new ArrayList<AltarRecipeJEI>();

        for (Map.Entry<ItemStack, AltarRecipeRegistry.AltarRecipe> itemStackAltarRecipeEntry : altarMap.entrySet()) {
            if (itemStackAltarRecipeEntry.getValue().getOutput() != null) { // Make sure output is not null. If it is, the recipe is for a filling orb, and we don't want that.
                ItemStack input = itemStackAltarRecipeEntry.getKey();
                ItemStack output = itemStackAltarRecipeEntry.getValue().getOutput();
                int requiredTier = itemStackAltarRecipeEntry.getValue().getMinTier().toInt();
                int requiredLP = itemStackAltarRecipeEntry.getValue().getSyphon();

                AltarRecipeJEI recipe = new AltarRecipeJEI(input, output, requiredTier, requiredLP);
                recipes.add(recipe);
            }
        }

        return recipes;
    }
}
