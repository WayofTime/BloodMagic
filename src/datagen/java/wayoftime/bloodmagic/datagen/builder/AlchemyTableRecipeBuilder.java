package wayoftime.bloodmagic.datagen.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.alchemytable.AlchemyTableRecipe;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipeBuilder {
    private final ItemStack output;
    private final List<Ingredient> inputs = new ArrayList<>();
    private int syphon = 0;
    private int ticks = 200;
    private int minimumTier = 0;

    private AlchemyTableRecipeBuilder(ItemStack output) {
        this.output = output;
    }

    public static AlchemyTableRecipeBuilder build(ItemLike output) {
        return new AlchemyTableRecipeBuilder(new ItemStack(output));
    }

    public static AlchemyTableRecipeBuilder build(ItemStack output) {
        return new AlchemyTableRecipeBuilder(output);
    }

    public AlchemyTableRecipeBuilder input(ItemLike item) {
        this.inputs.add(Ingredient.of(item));
        return this;
    }

    public AlchemyTableRecipeBuilder input(TagKey<Item> tag) {
        this.inputs.add(Ingredient.of(tag));
        return this;
    }

    public AlchemyTableRecipeBuilder input(Ingredient ingredient) {
        this.inputs.add(ingredient);
        return this;
    }

    public AlchemyTableRecipeBuilder syphon(int syphon) {
        this.syphon = syphon;
        return this;
    }

    public AlchemyTableRecipeBuilder ticks(int ticks) {
        this.ticks = ticks;
        return this;
    }

    public AlchemyTableRecipeBuilder minimumTier(int tier) {
        this.minimumTier = tier;
        return this;
    }

    public void save(RecipeOutput output, String name) {
        save(output, BloodMagic.rl("alchemytable/" + name));
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        AlchemyTableRecipe recipe = new AlchemyTableRecipe(inputs, output, syphon, ticks, minimumTier);
        recipeOutput.accept(id, recipe, null);
    }
}
