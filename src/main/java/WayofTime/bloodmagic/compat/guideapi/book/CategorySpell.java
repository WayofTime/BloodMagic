package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.util.Constants;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CategorySpell {
    public static Map<ResourceLocation, EntryAbstract> buildCategory() {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        String keyBase = Constants.Mod.DOMAIN + "spell_";

        return entries;
    }
}
