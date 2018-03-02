package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryDemon {
    //TODO: Add Forge recipe pages
    public static Map<ResourceLocation, EntryAbstract> buildCategory() {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        String keyBase = "guide." + BloodMagic.MODID + ".entry.demon.";

        List<IPage> introPages = new ArrayList<>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 370));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), true));

        List<IPage> snarePages = new ArrayList<>();
        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.1"), 370));

        IRecipe snareRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SOUL_SNARE));
        if (snareRecipe != null) {
            snarePages.add(BookUtils.getPageForRecipe(snareRecipe));
        }

        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "snare"), new EntryText(snarePages, TextHelper.localize(keyBase + "snare"), true));

        List<IPage> forgePages = new ArrayList<>();
        forgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "forge" + ".info.1"), 370));

        IRecipe forgeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.SOUL_FORGE));
        if (forgeRecipe != null) {
            forgePages.add(BookUtils.getPageForRecipe(forgeRecipe));
        }

        forgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "forge" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "forge"), new EntryText(forgePages, TextHelper.localize(keyBase + "forge"), true));

        List<IPage> pettyPages = new ArrayList<>();
        pettyPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "petty" + ".info.1"), 370));
        TartaricForgeRecipe pettyRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM));
        if (pettyRecipe != null) {
            pettyPages.add(new PageTartaricForgeRecipe(pettyRecipe));
        }
        pettyPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "petty" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "petty"), new EntryText(pettyPages, TextHelper.localize(keyBase + "petty"), true));

        List<IPage> swordPages = new ArrayList<>();
        swordPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sword" + ".info.1"), 370));
        TartaricForgeRecipe swordRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SWORD));
        if (swordRecipe != null) {
            swordPages.add(new PageTartaricForgeRecipe(swordRecipe));
        }
        swordPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sword" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "sword"), new EntryText(swordPages, TextHelper.localize(keyBase + "sword"), true));

        List<IPage> lesserPages = new ArrayList<>();
        lesserPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lesser" + ".info.1"), 370));
        TartaricForgeRecipe lesserRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1));
        if (lesserRecipe != null) {
            lesserPages.add(new PageTartaricForgeRecipe(lesserRecipe));
        }
        lesserPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lesser" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "lesser"), new EntryText(lesserPages, TextHelper.localize(keyBase + "lesser"), true));

        List<IPage> reactionsPages = new ArrayList<>();
        reactionsPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "reactions" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "reactions"), new EntryText(reactionsPages, TextHelper.localize(keyBase + "reactions"), true));

        List<IPage> sentientGemPages = new ArrayList<>();
        sentientGemPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sentientGem" + ".info.1"), 370));
        sentientGemPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sentientGem" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "sentientGem"), new EntryText(sentientGemPages, TextHelper.localize(keyBase + "sentientGem"), true));

        List<IPage> routingPages = new ArrayList<>();
        TartaricForgeRecipe nodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        if (nodeRecipe != null) {
            routingPages.add(new PageTartaricForgeRecipe(nodeRecipe));
        }
        TartaricForgeRecipe inputNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE));
        if (inputNodeRecipe != null) {
            routingPages.add(new PageTartaricForgeRecipe(inputNodeRecipe));
        }
        TartaricForgeRecipe outputNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE));
        if (outputNodeRecipe != null) {
            routingPages.add(new PageTartaricForgeRecipe(outputNodeRecipe));
        }
        TartaricForgeRecipe masterNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE));
        if (masterNodeRecipe != null) {
            routingPages.add(new PageTartaricForgeRecipe(masterNodeRecipe));
        }

        TartaricForgeRecipe nodeRouterRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.NODE_ROUTER));
        if (nodeRouterRecipe != null) {
            routingPages.add(new PageTartaricForgeRecipe(nodeRouterRecipe));
        }

        routingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "routing" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "routing"), new EntryText(routingPages, TextHelper.localize(keyBase + "routing"), true));

        List<IPage> auraPages = new ArrayList<>();

        auraPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "aura" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "aura"), new EntryText(auraPages, TextHelper.localize(keyBase + "aura"), true));

        List<IPage> typesPages = new ArrayList<>();

        typesPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "types" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "types"), new EntryText(typesPages, TextHelper.localize(keyBase + "types"), true));

        List<IPage> cruciblePages = new ArrayList<>();

        TartaricForgeRecipe crucibleRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRUCIBLE));
        if (crucibleRecipe != null) {
            cruciblePages.add(new PageTartaricForgeRecipe(crucibleRecipe));
        }

        cruciblePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "crucible" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "crucible"), new EntryText(cruciblePages, TextHelper.localize(keyBase + "crucible"), true));

        List<IPage> crystallizerPages = new ArrayList<>();

        TartaricForgeRecipe crystallizerRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTALLIZER));
        if (crystallizerRecipe != null) {
            crystallizerPages.add(new PageTartaricForgeRecipe(crystallizerRecipe));
        }

        crystallizerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "crystallizer" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "crystallizer"), new EntryText(crystallizerPages, TextHelper.localize(keyBase + "crystallizer"), true));

        List<IPage> clusterPages = new ArrayList<>();

        TartaricForgeRecipe clusterRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL));
        if (clusterRecipe != null) {
            clusterPages.add(new PageTartaricForgeRecipe(clusterRecipe));
        }

        clusterPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "cluster" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "cluster"), new EntryText(clusterPages, TextHelper.localize(keyBase + "cluster"), true));

        List<IPage> pylonPages = new ArrayList<>();

        TartaricForgeRecipe pylonRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.DEMON_PYLON));
        if (pylonRecipe != null) {
            pylonPages.add(new PageTartaricForgeRecipe(pylonRecipe));
        }

        pylonPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "pylon" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "pylon"), new EntryText(pylonPages, TextHelper.localize(keyBase + "pylon"), true));

        List<IPage> gaugePages = new ArrayList<>();

        TartaricForgeRecipe gaugeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.DEMON_WILL_GAUGE));
        if (gaugeRecipe != null) {
            gaugePages.add(new PageTartaricForgeRecipe(gaugeRecipe));
        }

        gaugePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "gauge" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "gauge"), new EntryText(gaugePages, TextHelper.localize(keyBase + "gauge"), true));

        for (Entry<ResourceLocation, EntryAbstract> entry : entries.entrySet()) {
            for (IPage page : entry.getValue().pageList) {
                if (page instanceof PageText) {
                    ((PageText) page).setUnicodeFlag(true);
                }
            }
        }

        return entries;
    }
}
