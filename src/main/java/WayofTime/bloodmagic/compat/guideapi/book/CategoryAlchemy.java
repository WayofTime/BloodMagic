package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
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
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        String keyBase = "guide." + BloodMagic.MODID + ".entry.alchemy.";

        List<IPage> introPages = new ArrayList<>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), true));

        List<IPage> ashPages = new ArrayList<>();

        TartaricForgeRecipe ashRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES));
        if (ashRecipe != null)
        {
            ashPages.add(new PageTartaricForgeRecipe(ashRecipe));
        }
        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), true));

        List<IPage> furnacePages = new ArrayList<>();

        PageAlchemyArray furnaceRecipePage = BookUtils.getAlchemyPage("furnace");
        if (furnaceRecipePage != null)
        {
            furnacePages.add(furnaceRecipePage);
        }
        furnacePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "furnace" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "furnace"), new EntryText(furnacePages, TextHelper.localize(keyBase + "furnace"), true));

        List<IPage> speedPages = new ArrayList<>();

        PageAlchemyArray speedRecipePage = BookUtils.getAlchemyPage("movement");
        if (speedRecipePage != null)
        {
            speedPages.add(speedRecipePage);
        }
        speedPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "speed" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "speed"), new EntryText(speedPages, TextHelper.localize(keyBase + "speed"), true));

        List<IPage> updraftPages = new ArrayList<>();

        PageAlchemyArray updraftRecipePage = BookUtils.getAlchemyPage("updraft");
        if (updraftRecipePage != null)
        {
            updraftPages.add(updraftRecipePage);
        }
        updraftPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "updraft" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "updraft"), new EntryText(updraftPages, TextHelper.localize(keyBase + "updraft"), true));

        List<IPage> turretPages = new ArrayList<>();

        PageAlchemyArray turretRecipePage = BookUtils.getAlchemyPage("skeletonTurret");
        if (turretRecipePage != null)
        {
            turretPages.add(turretRecipePage);
        }
        turretPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "turret" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "turret"), new EntryText(turretPages, TextHelper.localize(keyBase + "turret"), true));

        List<IPage> bouncePages = new ArrayList<>();

        PageAlchemyArray bounceRecipePage = BookUtils.getAlchemyPage("bounce");
        if (bounceRecipePage != null)
        {
            bouncePages.add(bounceRecipePage);
        }
        bouncePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bounce" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "bounce"), new EntryText(bouncePages, TextHelper.localize(keyBase + "bounce"), true));

        List<IPage> teleportPages = new ArrayList<>();

        PageAlchemyArray teleportRecipePage = BookUtils.getAlchemyPage("teleport");
        if (teleportRecipePage != null)
        {
            teleportPages.add(teleportRecipePage);
        }
        teleportPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleport" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "teleport"), new EntryText(teleportPages, TextHelper.localize(keyBase + "teleport"), true));

        List<IPage> standardTurretPages = new ArrayList<>();

        PageAlchemyArray standardTurretRecipePage = BookUtils.getAlchemyPage("turret");
        if (standardTurretRecipePage != null)
        {
            standardTurretPages.add(standardTurretRecipePage);
        }
        standardTurretPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "standardTurret" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "standardTurret"), new EntryText(standardTurretPages, TextHelper.localize(keyBase + "standardTurret"), true));

        List<IPage> laputaPages = new ArrayList<>();

        PageAlchemyArray laputaRecipePage = BookUtils.getAlchemyPage("laputa");
        if (laputaRecipePage != null)
        {
            laputaPages.add(laputaRecipePage);
        }
        laputaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "laputa" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "laputa"), new EntryText(laputaPages, TextHelper.localize(keyBase + "laputa"), true));

        List<IPage> buffPages = new ArrayList<>();

        buffPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "buff" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "buff"), new EntryText(buffPages, TextHelper.localize(keyBase + "buff"), true));

        List<IPage> fastMinerPages = new ArrayList<>();

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
