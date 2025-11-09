package wayoftime.bloodmagic.common.recipe.tiered;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public abstract class BaseTieredRecipe extends CustomRecipe {
    protected final ShapedRecipePattern pattern;
    protected final int primary;
    protected final int secondary;
    protected final ItemStack output;

    public BaseTieredRecipe(CraftingBookCategory category, ShapedRecipePattern pattern, int primary, int secondary, ItemStack baseOutput) {
        super(category);
        this.pattern = pattern;
        this.primary = primary;
        this.secondary = secondary;
        this.output = baseOutput;
    }

    public ShapedRecipePattern getPattern() {
        return this.pattern;
    }

    public int getPrimary() {
        return this.primary;
    }

    public int getSecondary() {
        return this.secondary;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if(!pattern.matches(input)) {
            return false;
        }

        // 0 means they dont have a tier
        int a = input.getItem(primary).getOrDefault(BMDataComponents.CONTAINER_TIER, 0);
        int b = input.getItem(secondary).getOrDefault(BMDataComponents.CONTAINER_TIER, 0);
        if (a == 0 || a == 16 || a != b) { // 0 = default = doesnt have tier; 16 = max tier; a != b means not same tier
            return false;
        }

        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return pattern.width() <= width && pattern.height() <= height;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
