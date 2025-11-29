package wayoftime.bloodmagic.common.recipe.alchemyarray;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.DoubleIngredientInput;

import javax.annotation.Nonnull;

public class AlchemyArrayRecipe implements Recipe<DoubleIngredientInput> {
    public static final String RECIPE_TYPE_NAME = "array";

    private final ResourceLocation texture;
    @Nonnull
    private final Ingredient baseInput;
    @Nonnull
    private final Ingredient addedInput;
    @Nonnull
    private final ItemStack output;

    public AlchemyArrayRecipe(ResourceLocation texture, @Nonnull Ingredient baseIngredient, @Nonnull Ingredient addedIngredient, @Nonnull ItemStack result) {
        this.texture = texture;
        this.baseInput = baseIngredient;
        this.addedInput = addedIngredient;
        this.output = result;
    }

    @Nonnull
    public ResourceLocation getTexture() {
        return texture;
    }

    @Nonnull
    public Ingredient getBaseInput() {
        return baseInput;
    }

    @Nonnull
    public Ingredient getAddedInput() {
        return addedInput;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(getBaseInput());
        list.add(getAddedInput());
        return list;
    }

    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(DoubleIngredientInput input, Level level) {
        return baseInput.test(input.first()) && addedInput.test(input.second());
    }

    @Override
    public ItemStack assemble(DoubleIngredientInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ALCHEMY_ARRAY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.ALCHEMY_ARRAY_TYPE.get();
    }
}
