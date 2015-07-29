//package WayofTime.alchemicalWizardry.common.tweaker;
//
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;
//import minetweaker.IUndoableAction;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IItemStack;
//import net.minecraft.item.ItemStack;
//import stanhebben.zenscript.annotations.Optional;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
//import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
//
///**
// * MineTweaker3 Blood Altar Recipe Handler by joshie *
// */
//@ZenClass("mods.bloodmagic.Altar")
//public class BloodAltar 
//{
//    @ZenMethod
//    public static void addRecipe(IItemStack output, IItemStack input, int tier, int lp, @Optional int consume, @Optional int drain) {
//        consume = consume > 0 ? consume : 20;
//        drain = drain > 0 ? drain : 20;
//        MineTweakerAPI.apply(new Add(new AltarRecipe(toStack(output), toStack(input), tier, lp, consume, drain, false)));
//    }
//
//    private static class Add implements IUndoableAction 
//    {
//        private final AltarRecipe recipe;
//        
//        public Add(AltarRecipe recipe) 
//        {
//            this.recipe = recipe;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            AltarRecipeRegistry.altarRecipes.add(recipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return AltarRecipeRegistry.altarRecipes != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            AltarRecipeRegistry.altarRecipes.remove(recipe);
//        }
//
//        @Override
//        public String describe() 
//        {
//            return "Adding Blood Altar Recipe for " + ((AltarRecipe) recipe).getResult().getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Removing Blood Altar Recipe for " + ((AltarRecipe) recipe).getResult().getDisplayName();
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
//    private static class Remove implements IUndoableAction 
//    {
//        private final ItemStack output;
//        private AltarRecipe recipe;
//        
//        public Remove(ItemStack output) 
//        {
//            this.output = output;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            for (AltarRecipe r : AltarRecipeRegistry.altarRecipes) 
//            {
//                if (r.getResult() != null && r.getResult().isItemEqual(output)) 
//                {
//                    recipe = r;
//                    break;
//                }
//            }
//
//            AltarRecipeRegistry.altarRecipes.remove(recipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return AltarRecipeRegistry.altarRecipes != null && recipe != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            AltarRecipeRegistry.altarRecipes.add(recipe);
//        }
//
//        @Override
//        public String describe() 
//        {
//            return "Removing Blood Altar Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Restoring Blood Altar Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey() 
//        {
//            return null;
//        }
//    }
//}
