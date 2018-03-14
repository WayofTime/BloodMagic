package WayofTime.bloodmagic.compat.jei.binding;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffect;
import WayofTime.bloodmagic.core.registry.AlchemyArrayRecipeRegistry;
import com.google.common.collect.BiMap;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BindingRecipeMaker {
    @Nonnull
    public static List<BindingRecipeJEI> getRecipes() {
        Map<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry.getRecipes();

        ArrayList<BindingRecipeJEI> recipes = new ArrayList<>();

        for (Map.Entry<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap.entrySet()) {
            List<ItemStack> input = itemStackAlchemyArrayRecipeEntry.getValue().getInput();
            BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry.getValue().catalystMap;

            for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet()) {
                ItemStack catalyst = entry.getKey().toStack();
                if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input, catalyst) instanceof AlchemyArrayEffectBinding) {
                    ItemStack output = ((AlchemyArrayEffectBinding) itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst)).outputStack;

                    BindingRecipeJEI recipe = new BindingRecipeJEI(input, catalyst, output);
                    recipes.add(recipe);
                }
            }
        }

        return recipes;
    }
}
