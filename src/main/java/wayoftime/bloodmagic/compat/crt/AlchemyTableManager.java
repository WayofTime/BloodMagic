package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.AlchemyTable")
public class AlchemyTableManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] input, int syphon, int ticks, int minimumTier) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeAlchemyTable recipe = new RecipeAlchemyTable(location, Arrays.stream(input).map(IIngredient::asVanillaIngredient).collect(Collectors.toList()), output.getInternal(), syphon, ticks, minimumTier);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public void removeRecipe(IItemStack output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for(ResourceLocation location : getManager().getRecipes().keySet()) {
                    Recipe<?> recipe = getManager().getRecipes().get(location);
                    if(recipe instanceof RecipeAlchemyTable) {
                        RecipeAlchemyTable recipeAT = (RecipeAlchemyTable) recipe;
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
    public RecipeType<RecipeAlchemyTable> getRecipeType() {
        return BloodMagicRecipeType.ALCHEMYTABLE;
    }
}
