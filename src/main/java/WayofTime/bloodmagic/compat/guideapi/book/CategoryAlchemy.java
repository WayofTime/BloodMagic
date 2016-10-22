package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryAlchemy
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.alchemy.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), true));

        List<IPage> ashPages = new ArrayList<IPage>();

        TartaricForgeRecipe ashRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.ARCANE_ASHES));
        if (ashRecipe != null)
        {
            ashPages.add(new PageTartaricForgeRecipe(ashRecipe));
        }
        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), true));

        List<IPage> speedPages = new ArrayList<IPage>();

        PageAlchemyArray speedRecipePage = BookUtils.getAlchemyPage("movement");
        if (speedRecipePage != null)
        {
            speedPages.add(speedRecipePage);
        }
        speedPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "speed" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "speed"), new EntryText(speedPages, TextHelper.localize(keyBase + "speed"), true));

        List<IPage> updraftPages = new ArrayList<IPage>();

        PageAlchemyArray updraftRecipePage = BookUtils.getAlchemyPage("updraft");
        if (updraftRecipePage != null)
        {
            updraftPages.add(updraftRecipePage);
        }
        updraftPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "updraft" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "updraft"), new EntryText(updraftPages, TextHelper.localize(keyBase + "updraft"), true));

        List<IPage> turretPages = new ArrayList<IPage>();

        PageAlchemyArray turretRecipePage = BookUtils.getAlchemyPage("skeletonTurret");
        if (turretRecipePage != null)
        {
            turretPages.add(turretRecipePage);
        }
        turretPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "turret" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "turret"), new EntryText(turretPages, TextHelper.localize(keyBase + "turret"), true));

        List<IPage> bouncePages = new ArrayList<IPage>();

        PageAlchemyArray bounceRecipePage = BookUtils.getAlchemyPage("bounce");
        if (bounceRecipePage != null)
        {
            bouncePages.add(bounceRecipePage);
        }
        bouncePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bounce" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "bounce"), new EntryText(bouncePages, TextHelper.localize(keyBase + "bounce"), true));

        List<IPage> buffPages = new ArrayList<IPage>();

        buffPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "buff" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "buff"), new EntryText(buffPages, TextHelper.localize(keyBase + "buff"), true));

        List<IPage> fastMinerPages = new ArrayList<IPage>();

        PageAlchemyArray fastMinerRecipePage = BookUtils.getAlchemyPage("fastMiner");
        if (fastMinerRecipePage != null)
        {
            fastMinerPages.add(fastMinerRecipePage);
        }
        fastMinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "fastMiner" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "fastMiner"), new EntryText(fastMinerPages, TextHelper.localize(keyBase + "fastMiner"), true));

        for (Entry<ResourceLocation, EntryAbstract> entry : entries.entrySet())
        {
            for (IPage page : entry.getValue().pageList)
            {
                if (page instanceof PageText)
                {
                    ((PageText) page).setUnicodeFlag(true);
                }
            }
        }

        return entries;
    }
}
