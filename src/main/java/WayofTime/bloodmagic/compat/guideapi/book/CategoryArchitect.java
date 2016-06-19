package WayofTime.bloodmagic.compat.guideapi.book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageIRecipe;

public class CategoryArchitect
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.architect.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 270));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), false));

        List<IPage> altarPages = new ArrayList<IPage>();

        IRecipe altarRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.altar));
        if (altarRecipe != null)
        {
            altarPages.add(new PageIRecipe(altarRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.1"), 270));

        IRecipe daggerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.sacrificialDagger));
        if (daggerRecipe != null)
        {
            altarPages.add(new PageIRecipe(daggerRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "bloodaltar"), new EntryText(altarPages, TextHelper.localize(keyBase + "bloodaltar"), false));

        List<IPage> ashPages = new ArrayList<IPage>();
        //TODO: Arcane Ash Recipe

        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), false));

        List<IPage> divinationPages = new ArrayList<IPage>();
        //TODO: Divination Sigil Recipe
        divinationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "divination" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "divination"), new EntryText(divinationPages, TextHelper.localize(keyBase + "divination"), false));

        List<IPage> soulnetworkPages = new ArrayList<IPage>();

        soulnetworkPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "soulnetwork" + ".info"), 270));
        entries.put(new ResourceLocation(keyBase + "soulnetwork"), new EntryText(soulnetworkPages, TextHelper.localize(keyBase + "soulnetwork"), false));

        List<IPage> weakorbPages = new ArrayList<IPage>();
        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.1"), 270));

        AltarRecipe weakorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbWeak));
        if (weakorbRecipe != null)
        {
            weakorbPages.add(new PageAltarRecipe(weakorbRecipe));
        }

        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "weakorb"), new EntryText(weakorbPages, TextHelper.localize(keyBase + "weakorb"), false));

        List<IPage> incensePages = new ArrayList<IPage>();

//        IRecipe incenseRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.incenseAltar));
//        if (incenseRecipe != null)
//        {
//            incensePages.add(new PageIRecipe(incenseRecipe));
//        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.1"), 270));

//        IRecipe woodPathRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.pathBlock, 1, 0));
//        if (woodPathRecipe != null)
//        {
//            incensePages.add(new PageIRecipe(woodPathRecipe));
//        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.2"), 270));
        entries.put(new ResourceLocation(keyBase + "incense"), new EntryText(incensePages, TextHelper.localize(keyBase + "incense"), false));

        return entries;
    }
}
