package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.apache.commons.lang3.tuple.Pair;
import org.openzen.zencode.java.ZenCodeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.ARC")
public class ARCManager implements IRecipeManager<RecipeARC> {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IFluidStack outputFluid, IIngredient input, IFluidStack inputFluid, IIngredient arcTool, boolean consumeIngredient, @ZenCodeType.Optional Percentaged<IItemStack>[] addedItems) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        List<Pair<ItemStack, Pair<Double, Double>>> addedItemsList = new ArrayList<>();
        if(addedItems != null) {
            addedItemsList = Arrays.stream(addedItems).map(mcWeightedItemStack -> Pair.of(mcWeightedItemStack.getData().getInternal(), Pair.of(mcWeightedItemStack.getPercentage(), mcWeightedItemStack.getPercentage()))).collect(Collectors.toList());
        }
        RecipeARC recipeARC = new RecipeARC(location, input.asVanillaIngredient(), 0, arcTool.asVanillaIngredient(), inputFluid.getInternal().isEmpty() ? null : FluidStackIngredient.from(inputFluid.getInternal()), output.getInternal(), addedItemsList, outputFluid.getInternal(), consumeIngredient);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipeARC, ""));
    }
    
    @Override
    public void remove(IIngredient output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for (int i = 0; i < getManager().getAllRecipes().size(); i++) {
                    ResourceLocation location = ARCManager.this.getRecipes().values().stream().toList().get(i).getId();
                    Recipe<?> recipe = getManager().getRecipeList().get(location);
                    if(recipe instanceof RecipeARC) {
                        RecipeARC recipeARC = (RecipeARC) recipe;
                        List<ItemStack> allListedOutputs = recipeARC.getAllListedOutputs();
                        // no other way to get the main output, and there may not even be a main output
                        if(allListedOutputs.size() > 0) {
                            if(output.matches(new MCItemStackMutable(allListedOutputs.get(0)))) {
                                toRemove.add(location);
                            }
                        }
                    }
                }
                toRemove.forEach(getManager().getRecipes()::remove);
            }
        });
    }
    
    @Override
    public RecipeType<RecipeARC> getRecipeType() {
        return BloodMagicRecipeType.ARC.get();
    }
}
