package wayoftime.bloodmagic.datagen.builder.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.recipe.forge.ForgeRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeRecipeBuilder extends BaseRecipeBuilder {

    protected double minWill;
    protected double drainedWill;
    protected List<Ingredient> ingredients = new ArrayList<>();
    protected boolean requireWillType = false;
    protected Optional<EnumWillType> willType = Optional.empty();

    protected ForgeRecipeBuilder(ItemStack result) {
        super(result);
    }

    public static ForgeRecipeBuilder build(ItemLike result) {
        return new ForgeRecipeBuilder(new ItemStack(result));
    }

    public ForgeRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(tag));
    }

    public ForgeRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    public ForgeRecipeBuilder requires(ItemLike item, int quantity) {
        this.requires(Ingredient.of(item), quantity);
        return this;
    }

    public ForgeRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public ForgeRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public ForgeRecipeBuilder minWill(double minWill) {
        this.minWill = minWill;
        return this;
    }

    public ForgeRecipeBuilder drain(double drain) {
        this.drainedWill = drain;
        return this;
    }

    public ForgeRecipeBuilder requiredWillType(EnumWillType type) {
        this.willType = Optional.of(type);
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advBuilder = getBuilder(output, id);
        ForgeRecipe recipe = new ForgeRecipe(minWill, drainedWill, ingredients, result, willType);
        output.accept(id.withPrefix("soul_forge/"), recipe, advBuilder.build(advancementId(id, "soulforge")));
    }
}
