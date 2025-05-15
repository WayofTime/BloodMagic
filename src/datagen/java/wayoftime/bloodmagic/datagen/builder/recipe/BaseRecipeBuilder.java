package wayoftime.bloodmagic.datagen.builder.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseRecipeBuilder implements RecipeBuilder {
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    protected String group;
    protected final ItemStack result;

    protected BaseRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    @Override
    public BaseRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public BaseRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    public Advancement.Builder getBuilder(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder advBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advBuilder::addCriterion);
        return advBuilder;
    }

    public ResourceLocation advancementId(ResourceLocation id, String folder) {
        return id.withPrefix("recipes/" + folder + "/");
    }
}
