package wayoftime.bloodmagic.common.recipe.arc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ARCRecipe implements Recipe<ARCRecipeInput> {

    public static final String RECIPE_TYPE_NAME = "alchemical_reaction_chamber";
    private final Ingredient tool;
    private final Ingredient input;
    private final List<ItemStack> guaranteedOutput;
    private final List<Pair<ItemStack, Double>> chanceOutput;
    private final Optional<FluidStack> inputFluid;
    private final Optional<FluidStack> outputFluid;
    private final List<Pair<ItemStack, Double>> allListed;
    public ARCRecipe(Ingredient tool, Ingredient input, List<ItemStack> guaranteedOutput, List<Pair<ItemStack, Double>> chanceOutput, Optional<FluidStack> inputFluid, Optional<FluidStack> outputStack) {
        this.tool = tool;
        this.input = input;
        this.guaranteedOutput = guaranteedOutput;
        this.chanceOutput = chanceOutput;
        this.inputFluid = inputFluid;
        this.outputFluid = outputStack;

        List<Pair<ItemStack, Double>> outputs = new ArrayList<>();
        guaranteedOutput.forEach(stack -> outputs.add(Pair.of(stack, 1D)));
        outputs.addAll(chanceOutput);
        allListed = List.copyOf(outputs);
    }

    public Ingredient getTool() {
        return tool;
    }

    public Ingredient getInput() {
        return input;
    }

    public List<ItemStack> getGuaranteedOutput() {
        return guaranteedOutput;
    }

    public List<Pair<ItemStack, Double>> getChanceOutput() {
        return chanceOutput;
    }

    public Optional<FluidStack> getInputFluid() {
        return inputFluid;
    }

    public Optional<FluidStack> getOutputFluid() {
        return outputFluid;
    }

    @Override
    public boolean matches(ARCRecipeInput recipeInput, Level level) {
        if (!tool.test(recipeInput.getItem(0))) {
            return false;
        }
        if (!input.test(recipeInput.getItem(1))) {
            return false;
        }
        if (inputFluid.isPresent()) {
            if (!(inputFluid.get().is(recipeInput.getFluid().getFluidType()) && inputFluid.get().getAmount() <= recipeInput.getFluid().getAmount())) {
                return false;
            }
        }
        return true;
    }

    private List<ItemStack> outputStacks = new ArrayList<>();
    private FluidStack outputFluidStack = FluidStack.EMPTY;
    @Override
    public ItemStack assemble(ARCRecipeInput input, HolderLookup.Provider registries) {
        outputStacks.clear();
        outputFluidStack = outputFluid.orElse(FluidStack.EMPTY);
        outputStacks.addAll(guaranteedOutput);
        ItemStack toolStack = input.getItem(0);
        double bonusChance = toolStack.getOrDefault(BMDataComponents.ARC_CHANCE, 1D);
        for (Pair<ItemStack, Double> entry : chanceOutput) {
            if (Math.random() < entry.getSecond() * bonusChance) {
                outputStacks.add(entry.getFirst());
            }
        }
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getActualOutputs() {
        return outputStacks;
    }

    public FluidStack getActualOutputFluid() {
        return outputFluidStack;
    }

    public List<Pair<ItemStack, Double>> getAllListedOutputs() {
        return allListed;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ARC_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.ARC_TYPE.get();
    }
}
