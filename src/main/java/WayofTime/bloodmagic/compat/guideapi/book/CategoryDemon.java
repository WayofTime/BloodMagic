package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;

public class CategoryDemon
{

    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new HashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.demon.";

        List<IPage> ritualPages = new ArrayList<IPage>();
        ritualPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 300));
//        ritualPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));

        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(ritualPages, TextHelper.localize("Test"), true));

        return entries;
    }
}
