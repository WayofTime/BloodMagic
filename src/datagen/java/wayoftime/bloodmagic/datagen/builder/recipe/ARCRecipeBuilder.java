package wayoftime.bloodmagic.datagen.builder.recipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import wayoftime.bloodmagic.common.blockentity.ARCTile;
import wayoftime.bloodmagic.common.recipe.arc.ARCRecipe;
import wayoftime.bloodmagic.datagen.builder.recipe.BaseRecipeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ARCRecipeBuilder extends BaseRecipeBuilder {

    private TagKey<Item> toolTag = null;
    private Ingredient input = null;
    private List<ItemStack> guaranteed = new ArrayList<>();
    private List<Pair<ItemStack, Double>> chanced = new ArrayList<>();
    private FluidStack inputFluid = null;
    private FluidStack outputFluid = null;

    protected ARCRecipeBuilder(TagKey<Item> tag) {
        super(ItemStack.EMPTY);
        toolTag = tag;
    }

    public static ARCRecipeBuilder build(TagKey<Item> tag) {
        return new ARCRecipeBuilder(tag);
    }

    public ARCRecipeBuilder input(ItemLike item) {
        return input(Ingredient.of(item));
    }

    public ARCRecipeBuilder input(ItemStack stack) {
        return input(Ingredient.of(stack));
    }

    public ARCRecipeBuilder input(Ingredient ingredient) {
        this.input = ingredient;
        return this;
    }

    public ARCRecipeBuilder guaranteedOutput(ItemStack output) {
        guaranteed.add(output);
        return this;
    }

    public ARCRecipeBuilder chancedOutput(ItemStack stack, double chance) {
        chanced.add(Pair.of(stack, chance));
        return this;
    }

    public ARCRecipeBuilder fluidInput(FluidStack fluidInput) {
        this.inputFluid = fluidInput;
        return this;
    }

    public ARCRecipeBuilder fluidOutput(FluidStack fluidOutput) {
        this.outputFluid = fluidOutput;
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advBuilder = getBuilder(output, id);
        Objects.requireNonNull(input);
        Objects.requireNonNull(toolTag);
        int maxOutputs = guaranteed.size() + chanced.size();
        assert maxOutputs > 0 && maxOutputs <= ARCTile.NUM_OUTPUTS;
        ARCRecipe recipe = new ARCRecipe(Ingredient.of(toolTag), input, guaranteed, chanced, Optional.ofNullable(inputFluid), Optional.ofNullable(outputFluid));
        output.accept(makeId(id, toolTag.location()), recipe, advBuilder.build(advancementId(id, "arc")));
    }

    private static ResourceLocation makeId(ResourceLocation id, ResourceLocation tag) {
        String[] segments = tag.getPath().split("/");
        return id.withPrefix("arc/" + segments[segments.length-1] + "/");
    }
}
