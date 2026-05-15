package wayoftime.bloodmagic.datagen.builder.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.recipe.bloodaltar.BloodAltarRecipe;

import java.util.Objects;

public class AltarRecipeBuilder extends BaseRecipeBuilder {

    protected int minTier = 0;
    protected Integer totalBlood = null;
    protected Integer craftingSpeed = null;
    protected Integer drainSpeed = null;
    protected Ingredient input = null;
    protected AltarRecipeBuilder(ItemStack result) {
        super(result);
    }

    public static AltarRecipeBuilder build(ItemLike result) {
        return new AltarRecipeBuilder(new ItemStack(result, 1));
    }

    public AltarRecipeBuilder minTier(int tier) {
        this.minTier = tier;
        return this;
    }

    public AltarRecipeBuilder bloodNeeded(int amount) {
        this.totalBlood = amount;
        return this;
    }

    public AltarRecipeBuilder consumption(int craftingSpeed) {
        this.craftingSpeed = craftingSpeed;
        return this;
    }

    public AltarRecipeBuilder drain(int drainSpeed) {
        this.drainSpeed = drainSpeed;
        return this;
    }

    public AltarRecipeBuilder from(ItemLike input) {
        return from(Ingredient.of(input));
    }

    public AltarRecipeBuilder from(TagKey<Item> input) {
        return from(Ingredient.of(input));
    }

    public AltarRecipeBuilder from(Ingredient input) {
        this.input = input;
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advBuilder = getBuilder(output, id);
        Objects.requireNonNull(input);
        Objects.requireNonNull(totalBlood);
        Objects.requireNonNull(craftingSpeed);
        Objects.requireNonNull(drainSpeed);
        BloodAltarRecipe recipe = new BloodAltarRecipe(input, result, minTier, totalBlood, craftingSpeed, drainSpeed);
        output.accept(id.withPrefix("blood_altar/"), recipe, advBuilder.build(advancementId(id, "blood_altar")));
    }
}
