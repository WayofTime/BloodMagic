package WayofTime.bloodmagic.compat.jei.alchemyArray;

import WayofTime.bloodmagic.apibutnotreally.ItemStackWrapper;
import WayofTime.bloodmagic.apibutnotreally.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.apibutnotreally.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.apibutnotreally.registry.AlchemyArrayRecipeRegistry;
import com.google.common.collect.BiMap;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlchemyArrayCraftingRecipeMaker {
    @Nonnull
    public static List<AlchemyArrayCraftingRecipeJEI> getRecipes() {
        Map<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry.getRecipes();

        ArrayList<AlchemyArrayCraftingRecipeJEI> recipes = new ArrayList<AlchemyArrayCraftingRecipeJEI>();

        for (Map.Entry<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap.entrySet()) {
            List<ItemStack> input = itemStackAlchemyArrayRecipeEntry.getValue().getInput();
            BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry.getValue().catalystMap;

            for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet()) {
                ItemStack catalyst = entry.getKey().toStack();
                if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input, catalyst) instanceof AlchemyArrayEffectCrafting) {
                    ItemStack output = ((AlchemyArrayEffectCrafting) itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst)).outputStack;

                    AlchemyArrayCraftingRecipeJEI recipe = new AlchemyArrayCraftingRecipeJEI(input, catalyst, output);
                    recipes.add(recipe);
                }
            }
        }

        return recipes;
    }
}
