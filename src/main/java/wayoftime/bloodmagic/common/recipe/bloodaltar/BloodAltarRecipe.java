package wayoftime.bloodmagic.common.recipe.bloodaltar;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

public class BloodAltarRecipe implements Recipe<BloodAltarInput> {

    public static final String RECIPE_TYPE_NAME = "blood_altar_recipe";
    private final ItemStack result;
    private final Ingredient input;
    public final int minTier;
    public final int totalBlood;
    public final int craftSpeed;
    public final int drainSpeed;
    public BloodAltarRecipe(Ingredient input, ItemStack result, int minTier, int totalBlood, int craftSpeed, int drainSpeed) {
        this.input = input;
        this.result = result;
        this.minTier = minTier;
        this.totalBlood = totalBlood;
        this.craftSpeed = craftSpeed;
        this.drainSpeed = drainSpeed;
    }

    public Ingredient getInput() {
        return input;
    }

    public int getCraftSpeed() {
        return craftSpeed;
    }

    public int getMinTier() {
        return minTier;
    }

    public int getTotalBlood() {
        return totalBlood;
    }

    public int getDrainSpeed() {
        return drainSpeed;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    @Override
    public boolean matches(BloodAltarInput recipeInput, Level level) {
        BloodMagic.LOGGER.info("matches called with {}, {}", recipeInput.getItem(0), recipeInput.getAltarTier());
        return minTier <= recipeInput.getAltarTier() && input.test(recipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(BloodAltarInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.BLOOD_ALTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.BLOOD_ALTAR_TYPE.get();
    }
}
