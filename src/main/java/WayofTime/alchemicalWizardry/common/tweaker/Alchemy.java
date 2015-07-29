//package WayofTime.alchemicalWizardry.common.tweaker;
//
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStacks;
//import net.minecraft.item.ItemStack;
//import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipe;
//import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
//
///**
// * MineTweaker3 Alchemy Recipe Handler by joshie *
// */
//@ZenClass("mods.bloodmagic.Alchemy")
//public class Alchemy 
//{
//    @ZenMethod
//    public static void addRecipe(IItemStack output, IItemStack[] input, int tier, int lp) {
//        MineTweakerAPI.apply(new Add(new AlchemyRecipe(toStack(output), (int) (((double) lp) / 100), toStacks(input), tier)));
//    }
//
//    private static class Add implements IUndoableAction 
//    {
//        private final AlchemyRecipe recipe;
//        
//        public Add(AlchemyRecipe recipe) 
//        {
//            this.recipe = recipe;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            AlchemyRecipeRegistry.recipes.add(recipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return AlchemyRecipeRegistry.recipes != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            AlchemyRecipeRegistry.recipes.remove(recipe);
//        }
//
//        @Override
//        public String describe() 
//        {
//            return "Adding Alchemy Recipe for " + ((AlchemyRecipe) recipe).getResult().getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Removing Alchemy Recipe for " + ((AlchemyRecipe) recipe).getResult().getDisplayName();
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
//        private AlchemyRecipe recipe;
//        
//        public Remove(ItemStack output) 
//        {
//            this.output = output;
//        }
//        
//        @Override
//        public void apply() 
//        {
//            for (AlchemyRecipe r : AlchemyRecipeRegistry.recipes) 
//            {
//                if (r.getResult() != null && r.getResult().isItemEqual(output)) 
//                {
//                    recipe = r;
//                    break;
//                }
//            }
//
//            AlchemyRecipeRegistry.recipes.remove(recipe);
//        }
//
//        @Override
//        public boolean canUndo() 
//        {
//            return AlchemyRecipeRegistry.recipes != null && recipe != null;
//        }
//
//        @Override
//        public void undo() 
//        {
//            AlchemyRecipeRegistry.recipes.add(recipe);
//        }
//
//        @Override
//        public String describe() 
//        {
//            return "Removing Alchemy Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo() 
//        {
//            return "Restoring Alchemy Recipe for " + output.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey() 
//        {
//            return null;
//        }
//    }
//}
