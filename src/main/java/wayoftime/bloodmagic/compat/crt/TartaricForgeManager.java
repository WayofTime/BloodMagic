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
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.TartaricForge")
public class TartaricForgeManager implements IRecipeManager<RecipeTartaricForge> {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] input, double minimumSouls, double soulDrain) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeTartaricForge recipe = new RecipeTartaricForge(location, Arrays.stream(input).map(IIngredient::asVanillaIngredient).collect(Collectors.toList()), output.getInternal(), minimumSouls, soulDrain);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public void remove(IIngredient output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for (int i = 0; i < getManager().getRecipes().size(); i++) {
                    ResourceLocation location = TartaricForgeManager.this.getRecipes().values().stream().toList().get(i).getId();
                    Recipe<?> recipe = getManager().getRecipeList().get(location);
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
    public RecipeType<RecipeTartaricForge> getRecipeType() {
        return BloodMagicRecipeType.TARTARICFORGE.get();
    }
}
