package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import wayoftime.bloodmagic.datagen.content.recipe.*;

import java.util.concurrent.CompletableFuture;

public class BMRecipeProvider extends RecipeProvider {
    public BMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        AlchemyTable.build(output, registries);
        ARC.build(output, registries);
        ArcaneAsh.build(output, registries);
        BloodAltar.build(output, registries);
        HellfireForge.build(output, registries);
    }
}
