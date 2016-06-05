package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageIRecipe;

public class CategoryDemon
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.demon.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 270));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), false));
//        CraftingManager.getInstance().g

        List<IPage> snarePages = new ArrayList<IPage>();
        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.1"), 270));

        IRecipe snareRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.soulSnare));
        if (snareRecipe != null)
        {
            snarePages.add(new PageIRecipe(snareRecipe));
        }

        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "snare"), new EntryText(snarePages, TextHelper.localize(keyBase + "snare"), false));

        return entries;
    }
}
