//package WayofTime.bloodmagic.compat.guideapi.book;
//
//import WayofTime.bloodmagic.api.Constants;
//import WayofTime.bloodmagic.api.registry.RitualRegistry;
//import WayofTime.bloodmagic.api.ritual.Ritual;
//import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
//import WayofTime.bloodmagic.util.helper.TextHelper;
//import amerifrance.guideapi.api.IPage;
//import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
//import amerifrance.guideapi.api.util.PageHelper;
//import amerifrance.guideapi.page.PageImage;
//import net.minecraft.util.ResourceLocation;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class CategoryRitual {
//
//    public static Map<ResourceLocation, EntryAbstract> buildCategory() {
//        Map<ResourceLocation, EntryAbstract> entries = new HashMap<ResourceLocation, EntryAbstract>();
//        String keyBase = Constants.Mod.DOMAIN + "ritual_";
//
//        for (Ritual ritual : RitualRegistry.getRituals()) {
//            List<IPage> ritualPages = new ArrayList<IPage>();
//            ritualPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(ritual.getUnlocalizedName() + ".desc")));
//            ritualPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
//            entries.put(new ResourceLocation(keyBase + ritual.getName()), new EntryText(ritualPages, TextHelper.localize(ritual.getUnlocalizedName()), true));
//        }
//
//        return entries;
//    }
//}
