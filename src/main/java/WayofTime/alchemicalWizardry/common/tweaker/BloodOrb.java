//package WayofTime.alchemicalWizardry.common.tweaker;
//
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toObjects;
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toShapedObjects;
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;
//
//import java.util.List;
//
//import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
//import minetweaker.IUndoableAction;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IIngredient;
//import minetweaker.api.item.IItemStack;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.CraftingManager;
//import net.minecraft.item.crafting.IRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
//
///**
// * MineTweaker3 Blood Orb Recipe Handler by joshie *
// */
//@ZenClass("mods.bloodmagic.BloodOrb")
//public class BloodOrb 
//{
//    @ZenMethod
//    public static void addShaped(IItemStack output, IIngredient[][] ingredients) 
//    {
//        MineTweakerAPI.apply(new Add(false, toStack(output), toShapedObjects(ingredients)));
//    }
//
//    @ZenMethod
//    public static void addShapeless(IItemStack output, IIngredient[] ingredients) 
//    {
//        MineTweakerAPI.apply(new Add(true, toStack(output), toObjects(ingredients)));
//    }
//
//    private static class Add implements IUndoableAction {
//        private IRecipe iRecipe;
//        private final boolean isShapeless;
//        private final ItemStack output;
//        private final Object[] recipe;
//        
//        public Add(boolean isShapeless, ItemStack output, Object... recipe) 
//        {
//            this.isShapeless = isShapeless;
//            this.output = output;
//            this.recipe = recipe;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            if (isShapeless) iRecipe = new ShapelessBloodOrbRecipe(output, recipe);
//            else iRecipe = new ShapedBloodOrbRecipe(output, recipe);
//            CraftingManager.getInstance().getRecipeList().add(iRecipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return CraftingManager.getInstance().getRecipeList() != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            CraftingManager.getInstance().getRecipeList().remove(iRecipe);
//        }
//
//        @Override
//        public String describe() {
//            return "Adding Blood Orb Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Removing Blood Orb Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey() 
//        {
//            return null;
//        }
//    }
//
//    @ZenMethod
//    public static void removeRecipe(IItemStack output) {
//        MineTweakerAPI.apply(new Remove(toStack(output)));
//    }
//
//    private static class Remove implements IUndoableAction {
//        private final ItemStack output;
//        private IRecipe iRecipe;
//        
//        public Remove(ItemStack output) 
//        {
//            this.output = output;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            for (IRecipe r : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) 
//            {
//                if((r instanceof ShapedBloodOrbRecipe || r instanceof ShapelessBloodOrbRecipe) && r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(output)) {
//                    iRecipe = r;
//                    break;
//                }
//            }
//            
//            CraftingManager.getInstance().getRecipeList().remove(iRecipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return CraftingManager.getInstance().getRecipeList() != null && iRecipe != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            CraftingManager.getInstance().getRecipeList().add(iRecipe);
//        }
//
//        @Override
//        public String describe() {
//            return "Removing Blood Orb Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Restoring Blood Orb Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey() 
//        {
//            return null;
//        }
//    }
//}
