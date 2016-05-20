//package WayofTime.bloodmagic.compat.guideapi;
//
//import WayofTime.bloodmagic.api.altar.EnumAltarTier;
//import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
//import WayofTime.bloodmagic.compat.ICompatibility;
//import amerifrance.guideapi.api.GuideAPI;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.common.registry.GameRegistry;
//
//public class CompatibilityGuideAPI implements ICompatibility {
//
//    @Override
//    public void loadCompatibility(InitializationPhase phase) {
//        switch (phase) {
//            case PRE_INIT: {
//                GuideBloodMagic.initBook();
//                AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BOOK), GuideAPI.getStackFromBook(GuideBloodMagic.guideBook), EnumAltarTier.ONE, 500, 2, 0));
//                break;
//            }
//            case INIT: {
//                break;
//            }
//            case POST_INIT: {
//                GuideBloodMagic.initCategories();
//                GameRegistry.register(GuideBloodMagic.guideBook);
//                break;
//            }
//        }
//    }
//
//    @Override
//    public String getModId() {
//        return "guideapi";
//    }
//
//    @Override
//    public boolean enableCompat() {
//        return true;
//    }
//}
