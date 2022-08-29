package wayoftime.bloodmagic.compat.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.AlchemyArray")
public class AlchemyArrayManager implements IRecipeManager<RecipeAlchemyArray> {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient baseInput, IIngredient addedInput, ResourceLocation texture) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeAlchemyArray recipe = new RecipeAlchemyArray(location, texture, baseInput.asVanillaIngredient(), addedInput.asVanillaIngredient(), output.getInternal());
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public void remove(IIngredient output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for (int i = 0; i < getManager().getRecipes().size(); i++) {
                    ResourceLocation location = AlchemyArrayManager.this.getRecipes().values().stream().toList().get(i).getId();
                    Recipe<?> recipe = getManager().getRecipeList().get(location);
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
    public RecipeType<RecipeAlchemyArray> getRecipeType() { return BloodMagicRecipeType.ARRAY.get(); }
}
