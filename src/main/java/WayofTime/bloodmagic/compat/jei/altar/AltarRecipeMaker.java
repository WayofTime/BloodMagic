package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

public class AltarRecipeMaker {

    @Nonnull
    public static List<AltarRecipeJEI> getAltarRecipes() {
        Map<ItemStack, AltarRecipe> altarMap = AltarRecipeRegistry.getRecipes();

        ArrayList<AltarRecipeJEI> recipes = new ArrayList<AltarRecipeJEI>();

        for (Map.Entry<ItemStack, AltarRecipe> itemStackAltarRecipeEntry : altarMap.entrySet()) {
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
