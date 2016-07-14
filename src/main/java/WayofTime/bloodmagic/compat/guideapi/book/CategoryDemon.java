package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageIRecipe;

public class CategoryDemon
{
    //TODO: Add Forge recipe pages
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.demon.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 270));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), false));

        List<IPage> snarePages = new ArrayList<IPage>();
        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.1"), 270));

        IRecipe snareRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.soulSnare));
        if (snareRecipe != null)
        {
            snarePages.add(new PageIRecipe(snareRecipe));
        }

        snarePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "snare" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "snare"), new EntryText(snarePages, TextHelper.localize(keyBase + "snare"), false));

        List<IPage> forgePages = new ArrayList<IPage>();
        forgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "forge" + ".info.1"), 270));

        IRecipe forgeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.soulForge));
        if (forgeRecipe != null)
        {
            forgePages.add(new PageIRecipe(forgeRecipe));
        }

        forgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "forge" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "forge"), new EntryText(forgePages, TextHelper.localize(keyBase + "forge"), false));

        List<IPage> pettyPages = new ArrayList<IPage>();
        pettyPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "petty" + ".info.1"), 270));
        TartaricForgeRecipe pettyRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.soulGem, 1));
        if (pettyRecipe != null)
        {
            pettyPages.add(new PageTartaricForgeRecipe(pettyRecipe));
        }
        pettyPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "petty" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "petty"), new EntryText(pettyPages, TextHelper.localize(keyBase + "petty"), false));

        List<IPage> swordPages = new ArrayList<IPage>();
        swordPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sword" + ".info.1"), 270));
        TartaricForgeRecipe swordRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.sentientSword));
        if (swordRecipe != null)
        {
            swordPages.add(new PageTartaricForgeRecipe(swordRecipe));
        }
        swordPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sword" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "sword"), new EntryText(swordPages, TextHelper.localize(keyBase + "sword"), false));

        List<IPage> lesserPages = new ArrayList<IPage>();
        lesserPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lesser" + ".info.1"), 270));
        TartaricForgeRecipe lesserRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.soulGem, 1));
        if (lesserRecipe != null)
        {
            lesserPages.add(new PageTartaricForgeRecipe(lesserRecipe));
        }
        lesserPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lesser" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "lesser"), new EntryText(lesserPages, TextHelper.localize(keyBase + "lesser"), false));

        List<IPage> reactionsPages = new ArrayList<IPage>();
        reactionsPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "reactions" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "reactions"), new EntryText(reactionsPages, TextHelper.localize(keyBase + "reactions"), false));

        List<IPage> sentientGemPages = new ArrayList<IPage>();
        sentientGemPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sentientGem" + ".info.1"), 270));
        sentientGemPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "sentientGem" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "sentientGem"), new EntryText(sentientGemPages, TextHelper.localize(keyBase + "sentientGem"), false));

        List<IPage> routingPages = new ArrayList<IPage>();
        TartaricForgeRecipe nodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModBlocks.itemRoutingNode));
        if (nodeRecipe != null)
        {
            routingPages.add(new PageTartaricForgeRecipe(nodeRecipe));
        }
        TartaricForgeRecipe inputNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModBlocks.itemRoutingNode));
        if (inputNodeRecipe != null)
        {
            routingPages.add(new PageTartaricForgeRecipe(inputNodeRecipe));
        }
        TartaricForgeRecipe outputNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModBlocks.outputRoutingNode));
        if (outputNodeRecipe != null)
        {
            routingPages.add(new PageTartaricForgeRecipe(outputNodeRecipe));
        }
        TartaricForgeRecipe masterNodeRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModBlocks.masterRoutingNode));
        if (masterNodeRecipe != null)
        {
            routingPages.add(new PageTartaricForgeRecipe(masterNodeRecipe));
        }

        TartaricForgeRecipe nodeRouterRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.nodeRouter));
        if (nodeRouterRecipe != null)
        {
            routingPages.add(new PageTartaricForgeRecipe(nodeRouterRecipe));
        }

        routingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "routing" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "routing"), new EntryText(routingPages, TextHelper.localize(keyBase + "routing"), false));

        return entries;
    }
}
