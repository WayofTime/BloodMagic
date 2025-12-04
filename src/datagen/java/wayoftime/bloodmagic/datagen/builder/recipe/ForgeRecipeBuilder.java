package wayoftime.bloodmagic.datagen.builder.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.common.datacomponent.AnointmentHolder;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
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

    public static ForgeRecipeBuilder build(ItemLike result, int count) {
        return new ForgeRecipeBuilder(new ItemStack(result, count));
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

    /**
     * Adds an anointment to the result item.
     * @param key The anointment key (e.g., "bloodmagic:fortune")
     * @param level The anointment level
     * @param maxDamage The max uses before the anointment expires
     */
    public ForgeRecipeBuilder withAnointment(String key, int level, int maxDamage) {
        this.result.set(BMDataComponents.ANOINTMENT_HOLDER.get(), AnointmentHolder.single(key, level, maxDamage));
        return this;
    }

    /**
     * Adds a data component to the result item.
     */
    public <T> ForgeRecipeBuilder withComponent(DataComponentType<T> component, T value) {
        this.result.set(component, value);
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advBuilder = getBuilder(output, id);
        ForgeRecipe recipe = new ForgeRecipe(minWill, drainedWill, ingredients, result, willType);
        output.accept(id.withPrefix("soul_forge/"), recipe, advBuilder.build(advancementId(id, "soulforge")));
    }
}
