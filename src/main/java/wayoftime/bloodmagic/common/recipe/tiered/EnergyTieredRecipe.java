package wayoftime.bloodmagic.common.recipe.tiered;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

public class EnergyTieredRecipe extends BaseTieredRecipe {
    public static final String NAME = "energy_tiered";

    public EnergyTieredRecipe(CraftingBookCategory category, ShapedRecipePattern pattern, int primary, int secondary, ItemStack baseOutput) {
        super(category, pattern, primary, secondary, baseOutput);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack primaryStack = input.getItem(primary);
        ItemStack secondaryStack = input.getItem(secondary);
        int primaryContent = primaryStack.getOrDefault(BMDataComponents.ENERGY_CONTENT, 0);
        int secondaryContent = secondaryStack.getOrDefault(BMDataComponents.ENERGY_CONTENT, 0);
        // add one to previous but not more than 16 -> make sure match returns false if input are 16 already
        int resultTier = Math.clamp(primaryStack.get(BMDataComponents.CONTAINER_TIER) + 1, 1, 16); // checked in super.matches

        int resultContent = primaryContent + secondaryContent;

        ItemStack resultStack = output.copy();
        resultStack.set(BMDataComponents.CONTAINER_TIER, resultTier);
        resultStack.set(BMDataComponents.ENERGY_CONTENT, resultContent);

        return resultStack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ENERGY_TIERED_SERIALIZER.get();
    }
}
