package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageText;

public class CategoryRitual
{
    static String keyBase = "guide." + Constants.Mod.MODID + ".entry.ritual.";

    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();

        addRitualPagesToEntries("intro", entries);
        addRitualPagesToEntries("basics", entries);

        List<IPage> ritualStonePages = new ArrayList<IPage>();

        IRecipe ritualStoneRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.RITUAL_STONE));
        if (ritualStoneRecipe != null)
        {
            ritualStonePages.add(BookUtils.getPageForRecipe(ritualStoneRecipe));
        }

        ritualStonePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ritualStone" + ".info.1"), 370));

        for (int i = 1; i < 5; i++)
        {
            EnumRuneType type = EnumRuneType.values()[i];
            AltarRecipe scribeRecipe = RecipeHelper.getAltarRecipeForOutput(type.getScribeStack());
            if (scribeRecipe != null)
            {
                ritualStonePages.add(new PageAltarRecipe(scribeRecipe));
            }
        }

        ritualStonePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ritualStone" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "ritualStone"), new EntryText(ritualStonePages, TextHelper.localize(keyBase + "ritualStone"), true));

        List<IPage> masterRitualStonePages = new ArrayList<IPage>();

        IRecipe masterRitualStoneRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.RITUAL_CONTROLLER, 1, 0));
        if (masterRitualStoneRecipe != null)
        {
            masterRitualStonePages.add(BookUtils.getPageForRecipe(masterRitualStoneRecipe));
        }

        masterRitualStonePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "masterRitualStone" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "masterRitualStone"), new EntryText(masterRitualStonePages, TextHelper.localize(keyBase + "masterRitualStone"), true));

        List<IPage> activationCrystalPages = new ArrayList<IPage>();

        activationCrystalPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "activationCrystal" + ".info.1"), 370));

        AltarRecipe crystalRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.ACTIVATION_CRYSTAL));
        if (crystalRecipe != null)
        {
            activationCrystalPages.add(new PageAltarRecipe(crystalRecipe));
        }

        activationCrystalPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "activationCrystal" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "activationCrystal"), new EntryText(activationCrystalPages, TextHelper.localize(keyBase + "activationCrystal"), true));

        List<IPage> divinerPages = new ArrayList<IPage>();

        divinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "diviner" + ".info.1"), 370));

        IRecipe divinerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.RITUAL_DIVINER));
        if (divinerRecipe != null)
        {
            divinerPages.add(BookUtils.getPageForRecipe(divinerRecipe));
        }

        divinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "diviner" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "diviner"), new EntryText(divinerPages, TextHelper.localize(keyBase + "diviner"), true));

        addRitualPagesToEntries("fullSpring", entries);
        addRitualPagesToEntries("lava", entries);
        addRitualPagesToEntries("greenGrove", entries);
        addRitualPagesToEntries("magnetism", entries);
        addRitualPagesToEntries("crusher", entries);
        addRitualPagesToEntries("highJump", entries);
        addRitualPagesToEntries("speed", entries);
        addRitualPagesToEntries("wellOfSuffering", entries);
        addRitualPagesToEntries("featheredKnife", entries);
        addRitualPagesToEntries("regen", entries);
        addRitualPagesToEntries("harvest", entries);
        addRitualPagesToEntries("interdiction", entries);
        addRitualPagesToEntries("containment", entries);
        addRitualPagesToEntries("suppression", entries);
        addRitualPagesToEntries("expulsion", entries);
        addRitualPagesToEntries("zephyr", entries);
        addRitualPagesToEntries("laying", entries);
        addRitualPagesToEntries("timberman", entries);
        addRitualPagesToEntries("meteor", entries);
        addRitualPagesToEntries("downgrade", entries);

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

    public static void addRitualPagesToEntries(String name, Map<ResourceLocation, EntryAbstract> entries)
    {
        List<IPage> pages = new ArrayList<IPage>();
        pages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + name + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + name), new EntryText(pages, TextHelper.localize(keyBase + name), true));
    }
}
