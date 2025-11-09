package wayoftime.bloodmagic.common.recipe.tiered;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

public class FluidTieredRecipe extends BaseTieredRecipe {
    public static final String NAME = "fluid_tiered";

    public FluidTieredRecipe(CraftingBookCategory category, ShapedRecipePattern pattern, int primary, int secondary, ItemStack baseOutput) {
        super(category, pattern, primary, secondary, baseOutput);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack primaryStack = input.getItem(primary);
        ItemStack secondaryStack = input.getItem(secondary);
        FluidStack primaryContent = primaryStack.getOrDefault(BMDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy();
        FluidStack secondaryContent = secondaryStack.getOrDefault(BMDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy();
        // add one to previous but not more than 16 -> make sure match returns false if input are 16 already
        int resultTier = Math.clamp(primaryStack.get(BMDataComponents.CONTAINER_TIER) + 1, 1, 16); // checked in super.matches

        // if same, add
        // if only one empty, take other
        // otherwise take primary
        FluidStack resultContent = primaryContent.copy();
        if (primaryContent.is(secondaryContent.getFluid())) {
            resultContent = primaryContent.copyWithAmount(primaryContent.getAmount() + secondaryContent.getAmount());
        } else if (primaryContent.isEmpty() && !secondaryContent.isEmpty()) {
            resultContent = secondaryContent.copy();
        } else if (!primaryContent.isEmpty() && secondaryContent.isEmpty()) {
            resultContent = primaryContent.copy();
        }

        ItemStack resultStack = output.copy();
        resultStack.set(BMDataComponents.CONTAINER_TIER, resultTier);
        resultStack.set(BMDataComponents.FLUID_CONTENT, SimpleFluidContent.copyOf(resultContent));

        return resultStack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.FLUID_TIERED_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return super.getType();
    }
}
