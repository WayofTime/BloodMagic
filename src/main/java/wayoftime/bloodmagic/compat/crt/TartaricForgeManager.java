package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
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
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.TartaricForge")
public class TartaricForgeManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] input, double minimumSouls, double soulDrain) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeTartaricForge recipe = new RecipeTartaricForge(location, Arrays.stream(input).map(IIngredient::asVanillaIngredient).collect(Collectors.toList()), output.getInternal(), minimumSouls, soulDrain);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public void removeRecipe(IItemStack output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for(ResourceLocation location : getManager().getRecipes().keySet()) {
                    IRecipe<?> recipe = getManager().getRecipes().get(location);
                    if(recipe instanceof RecipeTartaricForge) {
                        RecipeTartaricForge recipeTF = (RecipeTartaricForge) recipe;
                        ItemStack recipeOutput = recipeTF.getOutput();
                        if(output.matches(new MCItemStackMutable(recipeOutput))) {
                            toRemove.add(location);
                        }
                    }
                }
                toRemove.forEach(getManager().getRecipes()::remove);
            }
        });
    }
    
    @Override
    public IRecipeType<RecipeTartaricForge> getRecipeType() {
        return BloodMagicRecipeType.TARTARICFORGE;
    }
}
