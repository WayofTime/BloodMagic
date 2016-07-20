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
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapedBloodOrbRecipeRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapelessBloodOrbRecipeRenderer;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;

public class CategoryArchitect
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = "guide." + Constants.Mod.MODID + ".entry.architect.";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 370));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), true));

        List<IPage> altarPages = new ArrayList<IPage>();

        IRecipe altarRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.altar));
        if (altarRecipe != null)
        {
            altarPages.add(new PageIRecipe(altarRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.1"), 370));

        IRecipe daggerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.sacrificialDagger));
        if (daggerRecipe != null)
        {
            altarPages.add(new PageIRecipe(daggerRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "bloodaltar"), new EntryText(altarPages, TextHelper.localize(keyBase + "bloodaltar"), true));

        List<IPage> ashPages = new ArrayList<IPage>();

        TartaricForgeRecipe ashRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(ModItems.arcaneAshes));
        if (ashRecipe != null)
        {
            ashPages.add(new PageTartaricForgeRecipe(ashRecipe));
        }
        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), true));

        List<IPage> divinationPages = new ArrayList<IPage>();

        PageAlchemyArray divinationRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilDivination));
        if (divinationRecipePage != null)
        {
            divinationPages.add(divinationRecipePage);
        }

        divinationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "divination" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "divination"), new EntryText(divinationPages, TextHelper.localize(keyBase + "divination"), true));

        List<IPage> soulnetworkPages = new ArrayList<IPage>();

        soulnetworkPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "soulnetwork" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "soulnetwork"), new EntryText(soulnetworkPages, TextHelper.localize(keyBase + "soulnetwork"), true));

        List<IPage> weakorbPages = new ArrayList<IPage>();
        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.1"), 370));

        AltarRecipe weakorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbWeak));
        if (weakorbRecipe != null)
        {
            weakorbPages.add(new PageAltarRecipe(weakorbRecipe));
        }

        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "weakorb"), new EntryText(weakorbPages, TextHelper.localize(keyBase + "weakorb"), true));

        List<IPage> incensePages = new ArrayList<IPage>();

        IRecipe incenseRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.incenseAltar));
        if (incenseRecipe != null)
        {
            incensePages.add(getPageForRecipe(incenseRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.1"), 370));

        IRecipe woodPathRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.pathBlock, 1, 0));
        if (woodPathRecipe != null)
        {
            incensePages.add(getPageForRecipe(woodPathRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "incense"), new EntryText(incensePages, TextHelper.localize(keyBase + "incense"), true));

        List<IPage> runePages = new ArrayList<IPage>();

        IRecipe runeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 0));
        if (runeRecipe != null)
        {
            runePages.add(getPageForRecipe(runeRecipe));
        }

        runePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodrune" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "bloodrune"), new EntryText(runePages, TextHelper.localize(keyBase + "bloodrune"), true));

        List<IPage> inspectPages = new ArrayList<IPage>();

        AltarRecipe inspectRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.sanguineBook));
        if (inspectRecipe != null)
        {
            inspectPages.add(new PageAltarRecipe(inspectRecipe));
        }

        inspectPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "inspectoris" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "inspectoris"), new EntryText(inspectPages, TextHelper.localize(keyBase + "inspectoris"), true));

        List<IPage> speedRunePages = new ArrayList<IPage>();

        IRecipe speedRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 1));
        if (speedRecipe != null)
        {
            speedRunePages.add(getPageForRecipe(speedRecipe));
        }

        speedRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSpeed" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSpeed"), new EntryText(speedRunePages, TextHelper.localize(keyBase + "runeSpeed"), true));

        List<IPage> waterPages = new ArrayList<IPage>();

        TartaricForgeRecipe waterRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_WATER));
        if (waterRecipe != null)
        {
            waterPages.add(new PageTartaricForgeRecipe(waterRecipe));
        }

        PageAlchemyArray waterRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilWater));
        if (waterRecipePage != null)
        {
            waterPages.add(waterRecipePage);
        }

        waterPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "water" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "water"), new EntryText(waterPages, TextHelper.localize(keyBase + "water"), true));

        List<IPage> lavaPages = new ArrayList<IPage>();

        TartaricForgeRecipe lavaRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_LAVA));
        if (lavaRecipe != null)
        {
            lavaPages.add(new PageTartaricForgeRecipe(lavaRecipe));
        }

        PageAlchemyArray lavaRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilLava));
        if (lavaRecipePage != null)
        {
            lavaPages.add(lavaRecipePage);
        }

        lavaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lava" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lava"), new EntryText(lavaPages, TextHelper.localize(keyBase + "lava"), true));

        List<IPage> lavaCrystalPages = new ArrayList<IPage>();

        IRecipe lavaCrystalRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.lavaCrystal));
        if (lavaCrystalRecipe != null)
        {
            lavaCrystalPages.add(getPageForRecipe(lavaCrystalRecipe));
        }

        lavaCrystalPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lavaCrystal" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lavaCrystal"), new EntryText(lavaCrystalPages, TextHelper.localize(keyBase + "lavaCrystal"), true));

        List<IPage> apprenticeorbPages = new ArrayList<IPage>();

        AltarRecipe apprenticeorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbApprentice));
        if (apprenticeorbRecipe != null)
        {
            apprenticeorbPages.add(new PageAltarRecipe(apprenticeorbRecipe));
        }

        apprenticeorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "apprenticeorb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "apprenticeorb"), new EntryText(apprenticeorbPages, TextHelper.localize(keyBase + "apprenticeorb"), true));

        List<IPage> daggerPages = new ArrayList<IPage>();

        AltarRecipe daggerOfSacrificeRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.daggerOfSacrifice));
        if (daggerOfSacrificeRecipe != null)
        {
            daggerPages.add(new PageAltarRecipe(daggerOfSacrificeRecipe));
        }

        daggerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "dagger" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "dagger"), new EntryText(daggerPages, TextHelper.localize(keyBase + "dagger"), true));

        List<IPage> runeSacrificePages = new ArrayList<IPage>();

        IRecipe runeSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 3));
        if (runeSacrificeRecipe != null)
        {
            runeSacrificePages.add(getPageForRecipe(runeSacrificeRecipe));
        }

        runeSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSacrifice"), new EntryText(runeSacrificePages, TextHelper.localize(keyBase + "runeSacrifice"), true));

        List<IPage> runeSelfSacrificePages = new ArrayList<IPage>();

        IRecipe runeSelfSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (runeSelfSacrificeRecipe != null)
        {
            runeSelfSacrificePages.add(getPageForRecipe(runeSelfSacrificeRecipe));
        }

        runeSelfSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSelfSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSelfSacrifice"), new EntryText(runeSelfSacrificePages, TextHelper.localize(keyBase + "runeSelfSacrifice"), true));

        List<IPage> holdingPages = new ArrayList<IPage>();

        TartaricForgeRecipe holdingRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING));
        if (holdingRecipe != null)
        {
            holdingPages.add(new PageTartaricForgeRecipe(holdingRecipe));
        }

        PageAlchemyArray holdingRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilHolding));
        if (holdingRecipePage != null)
        {
            holdingPages.add(holdingRecipePage);
        }

        holdingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "holding" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "holding"), new EntryText(holdingPages, TextHelper.localize(keyBase + "holding"), true));

        List<IPage> airPages = new ArrayList<IPage>();

        TartaricForgeRecipe airRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_AIR));
        if (airRecipe != null)
        {
            airPages.add(new PageTartaricForgeRecipe(airRecipe));
        }

        PageAlchemyArray airRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilAir));
        if (airRecipePage != null)
        {
            airPages.add(airRecipePage);
        }

        airPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "air" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "air"), new EntryText(airPages, TextHelper.localize(keyBase + "air"), true));

        List<IPage> voidPages = new ArrayList<IPage>();

        TartaricForgeRecipe voidRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_VOID));
        if (voidRecipe != null)
        {
            voidPages.add(new PageTartaricForgeRecipe(voidRecipe));
        }

        PageAlchemyArray voidRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilVoid));
        if (voidRecipePage != null)
        {
            voidPages.add(voidRecipePage);
        }

        voidPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "void" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "void"), new EntryText(voidPages, TextHelper.localize(keyBase + "void"), true));

        List<IPage> greenGrovePages = new ArrayList<IPage>();

        TartaricForgeRecipe greenGroveRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH));
        if (greenGroveRecipe != null)
        {
            greenGrovePages.add(new PageTartaricForgeRecipe(greenGroveRecipe));
        }

        PageAlchemyArray greenGroveRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilGreenGrove));
        if (greenGroveRecipePage != null)
        {
            greenGrovePages.add(greenGroveRecipePage);
        }

        greenGrovePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "greenGrove" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "greenGrove"), new EntryText(greenGrovePages, TextHelper.localize(keyBase + "greenGrove"), true));

        List<IPage> fastMinerPages = new ArrayList<IPage>();

        TartaricForgeRecipe fastMinerRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER));
        if (fastMinerRecipe != null)
        {
            fastMinerPages.add(new PageTartaricForgeRecipe(fastMinerRecipe));
        }

        PageAlchemyArray fastMinerRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilFastMiner));
        if (fastMinerRecipePage != null)
        {
            fastMinerPages.add(fastMinerRecipePage);
        }

        fastMinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "fastMiner" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "fastMiner"), new EntryText(fastMinerPages, TextHelper.localize(keyBase + "fastMiner"), true));

        List<IPage> seerPages = new ArrayList<IPage>();

        TartaricForgeRecipe seerRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT));
        if (seerRecipe != null)
        {
            seerPages.add(new PageTartaricForgeRecipe(seerRecipe));
        }

        PageAlchemyArray seerRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilSeer));
        if (seerRecipePage != null)
        {
            seerPages.add(seerRecipePage);
        }

        seerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "seer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "seer"), new EntryText(seerPages, TextHelper.localize(keyBase + "seer"), true));

        List<IPage> magicianOrbPages = new ArrayList<IPage>();

        AltarRecipe magicianOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbMagician));
        if (magicianOrbRecipe != null)
        {
            magicianOrbPages.add(new PageAltarRecipe(magicianOrbRecipe));
        }

        magicianOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magicianOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magicianOrb"), new EntryText(magicianOrbPages, TextHelper.localize(keyBase + "magicianOrb"), true));

        List<IPage> capacityPages = new ArrayList<IPage>();

        IRecipe capacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (capacityRecipe != null)
        {
            capacityPages.add(getPageForRecipe(capacityRecipe));
        }

        capacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "capacity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "capacity"), new EntryText(capacityPages, TextHelper.localize(keyBase + "capacity"), true));

        List<IPage> displacementPages = new ArrayList<IPage>();

        IRecipe displacementRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (displacementRecipe != null)
        {
            displacementPages.add(getPageForRecipe(displacementRecipe));
        }

        displacementPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "displacement" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "displacement"), new EntryText(displacementPages, TextHelper.localize(keyBase + "displacement"), true));

        List<IPage> affinityPages = new ArrayList<IPage>();

        TartaricForgeRecipe affinityRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY));
        if (affinityRecipe != null)
        {
            affinityPages.add(new PageTartaricForgeRecipe(affinityRecipe));
        }

        PageAlchemyArray affinityRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilElementalAffinity));
        if (affinityRecipePage != null)
        {
            affinityPages.add(affinityRecipePage);
        }

        affinityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "affinity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "affinity"), new EntryText(affinityPages, TextHelper.localize(keyBase + "affinity"), true));

        List<IPage> lampPages = new ArrayList<IPage>();

        TartaricForgeRecipe lampRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT));
        if (lampRecipe != null)
        {
            lampPages.add(new PageTartaricForgeRecipe(lampRecipe));
        }

        PageAlchemyArray lampRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilBloodLight));
        if (lampRecipePage != null)
        {
            lampPages.add(lampRecipePage);
        }

        lampPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lamp" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lamp"), new EntryText(lampPages, TextHelper.localize(keyBase + "lamp"), true));

        List<IPage> magnetismPages = new ArrayList<IPage>();

        TartaricForgeRecipe magnetismRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM));
        if (magnetismRecipe != null)
        {
            magnetismPages.add(new PageTartaricForgeRecipe(magnetismRecipe));
        }

        PageAlchemyArray magnetismRecipePage = getAlchemyPage(new ItemStack(ModItems.sigilMagnetism));
        if (magnetismRecipePage != null)
        {
            magnetismPages.add(magnetismRecipePage);
        }

        magnetismPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magnetism" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magnetism"), new EntryText(magnetismPages, TextHelper.localize(keyBase + "magnetism"), true));

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

    public static PageAlchemyArray getAlchemyPage(String key)
    {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForArrayEffect(key);
        if (recipe[0] != null)
        {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer != null)
            {
                return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack);
            }
        }

        return null;
    }

    public static PageAlchemyArray getAlchemyPage(ItemStack outputStack)
    {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForOutputStack(outputStack);
        if (recipe[0] != null)
        {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer != null)
            {
                return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack, outputStack);
            }
        }

        return null;
    }

    public static PageIRecipe getPageForRecipe(IRecipe recipe)
    {
        if (recipe instanceof ShapedBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapedBloodOrbRecipeRenderer((ShapedBloodOrbRecipe) recipe));
        } else if (recipe instanceof ShapelessBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapelessBloodOrbRecipeRenderer((ShapelessBloodOrbRecipe) recipe));
        }

        return new PageIRecipe(recipe);
    }
}
