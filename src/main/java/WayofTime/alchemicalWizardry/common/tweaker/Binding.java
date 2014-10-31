package WayofTime.alchemicalWizardry.common.tweaker;

import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;

/**
 * MineTweaker3 Binding Recipe Handler by joshie *
 */
@ZenClass("mods.bloodmagic.Binding")
public class Binding 
{
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new Add(new BindingRecipe(toStack(output), toStack(input))));
    }

    private static class Add implements IUndoableAction 
    {
        private final BindingRecipe recipe;
        
        public Add(BindingRecipe recipe) 
        {
            this.recipe = recipe;
        }
        
        @Override
        public void apply() 
        {
            BindingRegistry.bindingRecipes.add(recipe);
        }

        @Override
        public boolean canUndo() 
        {
            return BindingRegistry.bindingRecipes != null;
        }

        @Override
        public void undo() 
        {
            BindingRegistry.bindingRecipes.remove(recipe);
        }

        @Override
        public String describe() 
        {
            return "Adding Binding Recipe for " + ((BindingRecipe) recipe).getResult().getDisplayName();
        }

        @Override
        public String describeUndo() 
        {
            return "Removing Binding Recipe for " + ((BindingRecipe) recipe).getResult().getDisplayName();
        }

        @Override
        public Object getOverrideKey() 
        {
            return null;
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove implements IUndoableAction 
    {
        private final ItemStack output;
        private BindingRecipe recipe;
        
        public Remove(ItemStack output) 
        {
            this.output = output;
        }
        
        @Override
        public void apply() 
        {
            for (BindingRecipe r : BindingRegistry.bindingRecipes) 
            {
                if (r.getResult() != null && r.getResult().isItemEqual(output)) 
                {
                    recipe = r;
                    break;
                }
            }

            BindingRegistry.bindingRecipes.remove(recipe);
        }

        @Override
        public boolean canUndo() 
        {
            return BindingRegistry.bindingRecipes != null && recipe != null;
        }

        @Override
        public void undo() 
        {
            BindingRegistry.bindingRecipes.add(recipe);
        }

        @Override
        public String describe() 
        {
            return "Removing Binding Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() 
        {
            return "Restoring Binding Recipe for " + output.getDisplayName();
        }

        @Override
        public Object getOverrideKey() 
        {
            return null;
        }
    }
}
