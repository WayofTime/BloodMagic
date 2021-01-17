package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.AlchemyArray")
public class AlchemyArrayManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient baseInput, IIngredient addedInput, ResourceLocation texture) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeAlchemyArray recipe = new RecipeAlchemyArray(location, texture, baseInput.asVanillaIngredient(), addedInput.asVanillaIngredient(), output.getInternal());
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
                    if(recipe instanceof RecipeAlchemyArray) {
                        RecipeAlchemyArray recipeAT = (RecipeAlchemyArray) recipe;
                        ItemStack recipeOutput = recipeAT.getOutput();
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
    public IRecipeType<RecipeAlchemyArray> getRecipeType() {
        return BloodMagicRecipeType.ARRAY;
    }
}
