//package WayofTime.alchemicalWizardry.common.tweaker;
//
//import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
//import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
//import minetweaker.IUndoableAction;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IItemStack;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.oredict.OreDictionary;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//import java.util.Iterator;
//
//import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;
//
///**
// * MineTweaker3 Falling Tower Paradigm Handler by hilburn *
// */
//@ZenClass("mods.bloodmagic.FallingTower")
//public class FallingTower
//{
//    @ZenMethod
//    public static void addFocus(IItemStack stack, int radius, String[] components)
//    {
//        MineTweakerAPI.apply(new Add(toStack(stack),radius, components));
//    }
//
//    @ZenMethod
//    public static void addFocus(IItemStack stack, int radius, String components)
//    {
//        MineTweakerAPI.apply(new Add(toStack(stack),radius, components.split("\\s*,\\s*")));
//    }
//
//    @ZenMethod
//    public static void removeFocus(IItemStack output) {
//        MineTweakerAPI.apply(new Remove(toStack(output)));
//    }
//
//    private static class Add implements IUndoableAction
//    {
//        private MeteorParadigm paradigm;
//
//        public Add(ItemStack stack, int radius, String[] components)
//        {
//            paradigm = new MeteorParadigm(stack,radius);
//            paradigm.parseStringArray(components);
//        }
//
//        @Override
//        public void apply()
//        {
//            MeteorRegistry.registerMeteorParadigm(paradigm);
//        }
//
//        @Override
//        public boolean canUndo()
//        {
//            return MeteorRegistry.paradigmList!= null;
//        }
//
//        @Override
//        public void undo()
//        {
//            MeteorRegistry.paradigmList.remove(paradigm);
//        }
//
//        @Override
//        public String describe() {
//            return "Adding Falling Tower Focus for " + paradigm.focusStack.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo()
//        {
//            return "Removing Falling Tower Focus for " + paradigm.focusStack.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey()
//        {
//            return null;
//        }
//    }
//
//    private static class Remove implements IUndoableAction {
//        private final ItemStack focus;
//        private MeteorParadigm paradigm;
//
//        public Remove(ItemStack focus)
//        {
//            this.focus = focus;
//        }
//
//        @Override
//        public void apply()
//        {
//            for (Iterator<MeteorParadigm> itr = MeteorRegistry.paradigmList.iterator(); itr.hasNext();)
//            {
//                MeteorParadigm paradigm = itr.next();
//                if (OreDictionary.itemMatches(paradigm.focusStack,focus,false))
//                {
//                    this.paradigm = paradigm;
//                    itr.remove();
//                    break;
//                }
//            }
//        }
//
//        @Override
//        public boolean canUndo()
//        {
//            return MeteorRegistry.paradigmList!= null && paradigm != null;
//        }
//
//        @Override
//        public void undo()
//        {
//            MeteorRegistry.paradigmList.add(paradigm);
//        }
//
//        @Override
//        public String describe() {
//            return "Removing Falling Tower Focus for " + focus.getDisplayName();
//        }
//
//        @Override
//        public String describeUndo()
//        {
//            return "Restoring Falling Tower Focus for " + focus.getDisplayName();
//        }
//
//        @Override
//        public Object getOverrideKey()
//        {
//            return null;
//        }
//    }
//}
