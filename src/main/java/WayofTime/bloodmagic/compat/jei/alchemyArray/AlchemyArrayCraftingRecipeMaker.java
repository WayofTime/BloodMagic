package WayofTime.bloodmagic.compat.jei.alchemyArray;

import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlchemyArrayCraftingRecipeMaker {

    @Nonnull
    public static List<AlchemyArrayCraftingRecipeJEI> getRecipes() {
        Map<ItemStack, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> altarMap = AlchemyArrayRecipeRegistry.getRecipes();

        ArrayList<AlchemyArrayCraftingRecipeJEI> recipes = new ArrayList<AlchemyArrayCraftingRecipeJEI>();

        for (Map.Entry<ItemStack, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : altarMap.entrySet()) {
            ItemStack input = itemStackAlchemyArrayRecipeEntry.getValue().getInputStack();
            ItemStack catalyst = itemStackAlchemyArrayRecipeEntry.getKey();

            if (itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst) instanceof AlchemyArrayEffectCrafting) {
                ItemStack output =((AlchemyArrayEffectCrafting) itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst)).getOutputStack();

                AlchemyArrayCraftingRecipeJEI recipe = new AlchemyArrayCraftingRecipeJEI(input, catalyst, output);
                recipes.add(recipe);
            }
        }

        return recipes;
    }
}
