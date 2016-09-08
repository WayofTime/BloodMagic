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
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
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
            altarPages.add(BookUtils.getPageForRecipe(altarRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.1"), 370));

        IRecipe daggerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.sacrificialDagger));
        if (daggerRecipe != null)
        {
            altarPages.add(BookUtils.getPageForRecipe(daggerRecipe));
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

        PageAlchemyArray divinationRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilDivination));
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
            incensePages.add(BookUtils.getPageForRecipe(incenseRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.1"), 370));

        IRecipe woodPathRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.pathBlock, 1, 0));
        if (woodPathRecipe != null)
        {
            incensePages.add(BookUtils.getPageForRecipe(woodPathRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "incense"), new EntryText(incensePages, TextHelper.localize(keyBase + "incense"), true));

        List<IPage> runePages = new ArrayList<IPage>();

        IRecipe runeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 0));
        if (runeRecipe != null)
        {
            runePages.add(BookUtils.getPageForRecipe(runeRecipe));
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
            speedRunePages.add(BookUtils.getPageForRecipe(speedRecipe));
        }

        speedRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSpeed" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSpeed"), new EntryText(speedRunePages, TextHelper.localize(keyBase + "runeSpeed"), true));

        List<IPage> waterPages = new ArrayList<IPage>();

        TartaricForgeRecipe waterRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_WATER));
        if (waterRecipe != null)
        {
            waterPages.add(new PageTartaricForgeRecipe(waterRecipe));
        }

        PageAlchemyArray waterRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilWater));
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

        PageAlchemyArray lavaRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilLava));
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
            lavaCrystalPages.add(BookUtils.getPageForRecipe(lavaCrystalRecipe));
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
            runeSacrificePages.add(BookUtils.getPageForRecipe(runeSacrificeRecipe));
        }

        runeSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSacrifice"), new EntryText(runeSacrificePages, TextHelper.localize(keyBase + "runeSacrifice"), true));

        List<IPage> runeSelfSacrificePages = new ArrayList<IPage>();

        IRecipe runeSelfSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (runeSelfSacrificeRecipe != null)
        {
            runeSelfSacrificePages.add(BookUtils.getPageForRecipe(runeSelfSacrificeRecipe));
        }

        runeSelfSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSelfSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSelfSacrifice"), new EntryText(runeSelfSacrificePages, TextHelper.localize(keyBase + "runeSelfSacrifice"), true));

        List<IPage> holdingPages = new ArrayList<IPage>();

        TartaricForgeRecipe holdingRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING));
        if (holdingRecipe != null)
        {
            holdingPages.add(new PageTartaricForgeRecipe(holdingRecipe));
        }

        PageAlchemyArray holdingRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilHolding));
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

        PageAlchemyArray airRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilAir));
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

        PageAlchemyArray voidRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilVoid));
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

        PageAlchemyArray greenGroveRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilGreenGrove));
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

        PageAlchemyArray fastMinerRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilFastMiner));
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

        PageAlchemyArray seerRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilSeer));
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
            capacityPages.add(BookUtils.getPageForRecipe(capacityRecipe));
        }

        capacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "capacity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "capacity"), new EntryText(capacityPages, TextHelper.localize(keyBase + "capacity"), true));

        List<IPage> displacementPages = new ArrayList<IPage>();

        IRecipe displacementRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 4));
        if (displacementRecipe != null)
        {
            displacementPages.add(BookUtils.getPageForRecipe(displacementRecipe));
        }

        displacementPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "displacement" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "displacement"), new EntryText(displacementPages, TextHelper.localize(keyBase + "displacement"), true));

        List<IPage> affinityPages = new ArrayList<IPage>();

        TartaricForgeRecipe affinityRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY));
        if (affinityRecipe != null)
        {
            affinityPages.add(new PageTartaricForgeRecipe(affinityRecipe));
        }

        PageAlchemyArray affinityRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilElementalAffinity));
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

        PageAlchemyArray lampRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilBloodLight));
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

        PageAlchemyArray magnetismRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilMagnetism));
        if (magnetismRecipePage != null)
        {
            magnetismPages.add(magnetismRecipePage);
        }

        magnetismPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magnetism" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magnetism"), new EntryText(magnetismPages, TextHelper.localize(keyBase + "magnetism"), true));

        List<IPage> peritiaPages = new ArrayList<IPage>();

        IRecipe peritiaRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModItems.experienceTome));
        if (peritiaRecipe != null)
        {
            peritiaPages.add(BookUtils.getPageForRecipe(peritiaRecipe));
        }

        peritiaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "peritia" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "peritia"), new EntryText(peritiaPages, TextHelper.localize(keyBase + "peritia"), true));

        List<IPage> livingArmourPages = new ArrayList<IPage>();

        TartaricForgeRecipe bindingRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_BINDING));
        if (bindingRecipe != null)
        {
            livingArmourPages.add(new PageTartaricForgeRecipe(bindingRecipe));
        }

        PageAlchemyArray bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.livingArmourChest));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.livingArmourHelmet));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.livingArmourLegs));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.livingArmourBoots));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        livingArmourPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "livingArmour" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "livingArmour"), new EntryText(livingArmourPages, TextHelper.localize(keyBase + "livingArmour"), true));

        List<IPage> upgradePages = new ArrayList<IPage>();

        upgradePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "upgradeTome" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "upgradeTome"), new EntryText(upgradePages, TextHelper.localize(keyBase + "upgradeTome"), true));

        List<IPage> teleposerPages = new ArrayList<IPage>();

        AltarRecipe teleposerFocusRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(ModItems.telepositionFocus));
        if (teleposerFocusRecipe != null)
        {
            teleposerPages.add(new PageAltarRecipe(teleposerFocusRecipe));
        }

        IRecipe teleposerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.teleposer));
        if (teleposerRecipe != null)
        {
            teleposerPages.add(BookUtils.getPageForRecipe(teleposerRecipe));
        }

        teleposerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposer"), new EntryText(teleposerPages, TextHelper.localize(keyBase + "teleposer"), true));

        List<IPage> boundBladePages = new ArrayList<IPage>();

        PageAlchemyArray boundBladePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.boundSword));
        if (boundBladePage != null)
        {
            boundBladePages.add(boundBladePage);
        }

        boundBladePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundBlade" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundBlade"), new EntryText(boundBladePages, TextHelper.localize(keyBase + "boundBlade"), true));

        List<IPage> boundToolPages = new ArrayList<IPage>();

        PageAlchemyArray boundToolPage = BookUtils.getAlchemyPage(new ItemStack(ModItems.boundPickaxe));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(ModItems.boundAxe));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(ModItems.boundShovel));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundTool" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundTool"), new EntryText(boundToolPages, TextHelper.localize(keyBase + "boundTool"), true));

        List<IPage> weakShardPages = new ArrayList<IPage>();

        weakShardPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakShard" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "weakShard"), new EntryText(weakShardPages, TextHelper.localize(keyBase + "weakShard"), true));

        List<IPage> masterOrbPages = new ArrayList<IPage>();

        AltarRecipe masterOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(ModItems.orbMaster));
        if (magicianOrbRecipe != null)
        {
            masterOrbPages.add(new PageAltarRecipe(masterOrbRecipe));
        }

        masterOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "masterOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "masterOrb"), new EntryText(masterOrbPages, TextHelper.localize(keyBase + "masterOrb"), true));

        List<IPage> orbRunePages = new ArrayList<IPage>();

        IRecipe orbRuneRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.bloodRune, 1, 8));
        if (orbRuneRecipe != null)
        {
            orbRunePages.add(BookUtils.getPageForRecipe(orbRuneRecipe));
        }

        orbRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeOrb"), new EntryText(orbRunePages, TextHelper.localize(keyBase + "runeOrb"), true));

        List<IPage> suppressionPages = new ArrayList<IPage>();

        TartaricForgeRecipe suppressionRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION));
        if (suppressionRecipe != null)
        {
            suppressionPages.add(new PageTartaricForgeRecipe(suppressionRecipe));
        }

        PageAlchemyArray suppressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilSuppression));
        if (suppressionRecipePage != null)
        {
            suppressionPages.add(suppressionRecipePage);
        }

        suppressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "suppression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "suppression"), new EntryText(suppressionPages, TextHelper.localize(keyBase + "suppression"), true));

        List<IPage> hastePages = new ArrayList<IPage>();

        TartaricForgeRecipe hasteRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_HASTE));
        if (hasteRecipe != null)
        {
            hastePages.add(new PageTartaricForgeRecipe(hasteRecipe));
        }

        PageAlchemyArray hasteRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilHaste));
        if (hasteRecipePage != null)
        {
            hastePages.add(hasteRecipePage);
        }

        hastePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "haste" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "haste"), new EntryText(hastePages, TextHelper.localize(keyBase + "haste"), true));

        List<IPage> severancePages = new ArrayList<IPage>();

        TartaricForgeRecipe severanceRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE));
        if (severanceRecipe != null)
        {
            severancePages.add(new PageTartaricForgeRecipe(severanceRecipe));
        }

        PageAlchemyArray severanceRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilEnderSeverance));
        if (severanceRecipePage != null)
        {
            severancePages.add(severanceRecipePage);
        }

        severancePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "severance" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "severance"), new EntryText(severancePages, TextHelper.localize(keyBase + "severance"), true));

        List<IPage> telepositionPages = new ArrayList<IPage>();

        TartaricForgeRecipe telepositionRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION));
        if (telepositionRecipe != null)
        {
            telepositionPages.add(new PageTartaricForgeRecipe(telepositionRecipe));
        }

        PageAlchemyArray telepositionRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilTeleposition));
        if (telepositionRecipePage != null)
        {
            telepositionPages.add(telepositionRecipePage);
        }

        telepositionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposition" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposition"), new EntryText(telepositionPages, TextHelper.localize(keyBase + "teleposition"), true));

        List<IPage> compressionPages = new ArrayList<IPage>();

        TartaricForgeRecipe compressionRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION));
        if (compressionRecipe != null)
        {
            compressionPages.add(new PageTartaricForgeRecipe(compressionRecipe));
        }

        PageAlchemyArray compressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilCompression));
        if (compressionRecipePage != null)
        {
            compressionPages.add(compressionRecipePage);
        }

        compressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "compression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "compression"), new EntryText(compressionPages, TextHelper.localize(keyBase + "compression"), true));

        List<IPage> bridgePages = new ArrayList<IPage>();

        TartaricForgeRecipe bridgeRecipe = RecipeHelper.getForgeRecipeForOutput(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE));
        if (bridgeRecipe != null)
        {
            bridgePages.add(new PageTartaricForgeRecipe(bridgeRecipe));
        }

        PageAlchemyArray bridgeRecipePage = BookUtils.getAlchemyPage(new ItemStack(ModItems.sigilPhantomBridge));
        if (bridgeRecipePage != null)
        {
            bridgePages.add(bridgeRecipePage);
        }

        bridgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bridge" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "bridge"), new EntryText(bridgePages, TextHelper.localize(keyBase + "bridge"), true));

        List<IPage> mimicPages = new ArrayList<IPage>();

        IRecipe mimicRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(ModBlocks.mimic, 1, 1));
        if (mimicRecipe != null)
        {
            mimicPages.add(BookUtils.getPageForRecipe(mimicRecipe));
        }

        mimicPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "mimic" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "mimic"), new EntryText(mimicPages, TextHelper.localize(keyBase + "mimic"), true));

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
