package wayoftime.bloodmagic.datagen.content.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.datagen.builder.recipe.AltarRecipeBuilder;

public class BloodAltar {

    public static void build(RecipeOutput output, HolderLookup.Provider registries) {
        AltarRecipeBuilder.build(BMItems.SLATE_IMBUED.get())
                .bloodNeeded(5000)
                .consumption(15)
                .drain(10)
                .from(BMItems.SLATE_REINFORCED.get())
                .minTier(3)
                .save(output, BMItems.SLATE_IMBUED.getId());
    }
}
