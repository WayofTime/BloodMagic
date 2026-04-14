package wayoftime.bloodmagic.datagen.builder.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.common.recipe.alchemy_table.AlchemyTableRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static wayoftime.bloodmagic.common.blockentity.AlchemyTableTile.INPUT_COUNT;

public class AlchemyTableRecipeBuilder extends BaseRecipeBuilder {

    protected List<Ingredient> inputs = new ArrayList<>();
    protected Integer tier = null;
    protected int duration = 100;
    protected Integer essence = null;

    protected AlchemyTableRecipeBuilder(ItemStack result) {
        super(result);
    }

    public AlchemyTableRecipeBuilder requires(Ingredient input) {
        inputs.add(input);
        return this;
    }

    public AlchemyTableRecipeBuilder requires(TagKey<Item> input) {
        return requires(Ingredient.of(input));
    }

    public AlchemyTableRecipeBuilder requires(ItemLike input) {
        return requires(Ingredient.of(input));
    }

    public AlchemyTableRecipeBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    public AlchemyTableRecipeBuilder essence(int essence) {
        this.essence = essence;
        return this;
    }

    public AlchemyTableRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advBuilder = getBuilder(output, id);
        Objects.requireNonNull(essence);
        Objects.requireNonNull(tier);
        int size = inputs.size();
        assert size > 0 && size <= INPUT_COUNT;
        AlchemyTableRecipe recipe = new AlchemyTableRecipe(inputs, tier, essence, duration, result);
        output.accept(id.withPrefix("alchemy_table/"), recipe, advBuilder.build(advancementId(id, "alchemy_table")));
    }
}
