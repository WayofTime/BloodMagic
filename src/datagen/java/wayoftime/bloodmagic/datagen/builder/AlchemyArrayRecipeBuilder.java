package wayoftime.bloodmagic.datagen.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.alchemyarray.AlchemyArrayRecipe;

public class AlchemyArrayRecipeBuilder {
    private final ItemStack output;
    private Ingredient baseInput;
    private Ingredient addedInput;
    private ResourceLocation texture;

    private AlchemyArrayRecipeBuilder(ItemStack output) {
        this.output = output;
        this.texture = BloodMagic.rl("textures/models/alchemyarrays/sigil.png");
    }

    public static AlchemyArrayRecipeBuilder build(ItemLike output) {
        return new AlchemyArrayRecipeBuilder(new ItemStack(output));
    }

    public static AlchemyArrayRecipeBuilder build(ItemStack output) {
        return new AlchemyArrayRecipeBuilder(output);
    }

    public AlchemyArrayRecipeBuilder base(ItemLike item) {
        this.baseInput = Ingredient.of(item);
        return this;
    }

    public AlchemyArrayRecipeBuilder base(Ingredient ingredient) {
        this.baseInput = ingredient;
        return this;
    }

    public AlchemyArrayRecipeBuilder added(ItemLike item) {
        this.addedInput = Ingredient.of(item);
        return this;
    }

    public AlchemyArrayRecipeBuilder added(Ingredient ingredient) {
        this.addedInput = ingredient;
        return this;
    }

    public AlchemyArrayRecipeBuilder texture(String path) {
        this.texture = BloodMagic.rl(path);
        return this;
    }

    public AlchemyArrayRecipeBuilder texture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public void save(RecipeOutput output, String name) {
        save(output, BloodMagic.rl("array/" + name));
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        AlchemyArrayRecipe recipe = new AlchemyArrayRecipe(texture, baseInput, addedInput, output);
        recipeOutput.accept(id, recipe, null);
    }
}
