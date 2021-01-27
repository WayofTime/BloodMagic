package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveOutputRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import com.blamejared.crafttweaker.impl.item.MCWeightedItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
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
public class ARCManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IFluidStack outputFluid, IIngredient input, IFluidStack inputFluid, IIngredient arcTool, boolean consumeIngredient, @ZenCodeType.Optional MCWeightedItemStack[] addedItems) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        List<Pair<ItemStack, Double>> addedItemsList = new ArrayList<>();
        if(addedItems != null) {
            addedItemsList = Arrays.stream(addedItems).map(mcWeightedItemStack -> Pair.of(mcWeightedItemStack.getItemStack().getInternal(), mcWeightedItemStack.getWeight())).collect(Collectors.toList());
        }
        RecipeARC recipeARC = new RecipeARC(location, input.asVanillaIngredient(), arcTool.asVanillaIngredient(), inputFluid.getInternal().isEmpty() ? null : FluidStackIngredient.from(inputFluid.getInternal()), output.getInternal(), addedItemsList, outputFluid.getInternal(), consumeIngredient);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipeARC, ""));
    }
    
    @Override
    public void removeRecipe(IItemStack output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for(ResourceLocation location : getManager().getRecipes().keySet()) {
                    IRecipe<?> recipe = getManager().getRecipes().get(location);
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
    public IRecipeType<RecipeARC> getRecipeType() {
        return BloodMagicRecipeType.ARC;
    }
}
