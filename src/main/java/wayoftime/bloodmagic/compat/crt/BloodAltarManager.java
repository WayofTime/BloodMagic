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
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.bloodmagic.BloodAltar")
public class BloodAltarManager implements IRecipeManager<RecipeBloodAltar> {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient input, int minimumTier, int syphon, int consumeRate, int drainRate) {
        name = fixRecipeName(name);
        ResourceLocation location = new ResourceLocation("crafttweaker", name);
        RecipeBloodAltar recipeARC = new RecipeBloodAltar(location, input.asVanillaIngredient(), output.getInternal(), minimumTier, syphon, consumeRate, drainRate);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipeARC, ""));
    }
    
    @Override
    public void remove(IIngredient output) {
        
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output) {
            @Override
            public void apply() {
                List<ResourceLocation> toRemove = new ArrayList<>();
                for (int i = 0; i < getManager().getRecipes().size(); i++) {
                    ResourceLocation location = BloodAltarManager.this.getRecipes().values().stream().toList().get(i).getId();
                    Recipe<?> recipe = getManager().getRecipeList().get(location);
                    if(recipe instanceof RecipeBloodAltar) {
                        RecipeBloodAltar recipeBloodAltar = (RecipeBloodAltar) recipe;
                        ItemStack recOut = recipeBloodAltar.getOutput();
                        if(output.matches(new MCItemStackMutable(recOut))) {
                            toRemove.add(location);
                        }
                    }
                }
                toRemove.forEach(getManager().getRecipes()::remove);
            }
        });
    }
    
    @Override
    public RecipeType<RecipeBloodAltar> getRecipeType() {
        return BloodMagicRecipeType.ALTAR.get();
    }
}
