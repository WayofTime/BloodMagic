package WayofTime.bloodmagic.compat.jei.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;

import com.google.common.collect.BiMap;

public class BindingRecipeMaker
{
    @Nonnull
    public static List<BindingRecipeJEI> getRecipes()
    {
        Map<ItemStackWrapper, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry.getRecipes();

        ArrayList<BindingRecipeJEI> recipes = new ArrayList<BindingRecipeJEI>();

        for (Map.Entry<ItemStackWrapper, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap.entrySet())
        {
            ItemStack input = itemStackAlchemyArrayRecipeEntry.getValue().getInputStack();
            BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry.getValue().catalystMap;

            for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet())
            {
                ItemStack catalyst = entry.getKey().toStack();
                if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input, catalyst) instanceof AlchemyArrayEffectBinding)
                {
                    ItemStack output = ((AlchemyArrayEffectBinding) itemStackAlchemyArrayRecipeEntry.getValue().getAlchemyArrayEffectForCatalyst(catalyst)).getOutputStack();

                    BindingRecipeJEI recipe = new BindingRecipeJEI(input, catalyst, output);
                    recipes.add(recipe);
                }
            }
        }

        return recipes;
    }
}
