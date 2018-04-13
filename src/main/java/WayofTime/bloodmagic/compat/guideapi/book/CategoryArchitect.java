package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.core.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ComponentTypes;
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

public class CategoryArchitect
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        String keyBase = "guide." + BloodMagic.MODID + ".entry.architect.";

        List<IPage> introPages = new ArrayList<>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "intro" + ".info"), 370));
//        introPages.add(new PageImage(new ResourceLocation("bloodmagicguide", "textures/guide/" + ritual.getName() + ".png")));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, TextHelper.localize(keyBase + "intro"), true));

        List<IPage> altarPages = new ArrayList<>();

        IRecipe altarRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.ALTAR));
        if (altarRecipe != null)
        {
            altarPages.add(BookUtils.getPageForRecipe(altarRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.1"), 370));

        IRecipe daggerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SACRIFICIAL_DAGGER));
        if (daggerRecipe != null)
        {
            altarPages.add(BookUtils.getPageForRecipe(daggerRecipe));
        }

        altarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodaltar" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "bloodaltar"), new EntryText(altarPages, TextHelper.localize(keyBase + "bloodaltar"), true));

        List<IPage> ashPages = new ArrayList<>();

        TartaricForgeRecipe ashRecipe = RecipeHelper.getForgeRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES));
        if (ashRecipe != null)
        {
            ashPages.add(new PageTartaricForgeRecipe(ashRecipe));
        }
        ashPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "ash" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "ash"), new EntryText(ashPages, TextHelper.localize(keyBase + "ash"), true));

        List<IPage> divinationPages = new ArrayList<>();

        PageAlchemyArray divinationRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION));
        if (divinationRecipePage != null)
        {
            divinationPages.add(divinationRecipePage);
        }

        divinationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "divination" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "divination"), new EntryText(divinationPages, TextHelper.localize(keyBase + "divination"), true));

        List<IPage> soulnetworkPages = new ArrayList<>();

        soulnetworkPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "soulnetwork" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "soulnetwork"), new EntryText(soulnetworkPages, TextHelper.localize(keyBase + "soulnetwork"), true));

        List<IPage> weakorbPages = new ArrayList<>();
        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.1"), 370));

        AltarRecipe weakorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_WEAK));
        if (weakorbRecipe != null)
        {
            weakorbPages.add(new PageAltarRecipe(weakorbRecipe));
        }

        weakorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakorb" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "weakorb"), new EntryText(weakorbPages, TextHelper.localize(keyBase + "weakorb"), true));

        List<IPage> incensePages = new ArrayList<>();

        IRecipe incenseRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.INCENSE_ALTAR));
        if (incenseRecipe != null)
        {
            incensePages.add(BookUtils.getPageForRecipe(incenseRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.1"), 370));

        IRecipe woodPathRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.PATH, 1, 0));
        if (woodPathRecipe != null)
        {
            incensePages.add(BookUtils.getPageForRecipe(woodPathRecipe));
        }

        incensePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "incense" + ".info.2"), 370));
        entries.put(new ResourceLocation(keyBase + "incense"), new EntryText(incensePages, TextHelper.localize(keyBase + "incense"), true));

        List<IPage> runePages = new ArrayList<>();

        IRecipe runeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 0));
        if (runeRecipe != null)
        {
            runePages.add(BookUtils.getPageForRecipe(runeRecipe));
        }

        runePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bloodrune" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "bloodrune"), new EntryText(runePages, TextHelper.localize(keyBase + "bloodrune"), true));

        List<IPage> inspectPages = new ArrayList<>();

        AltarRecipe inspectRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK));
        if (inspectRecipe != null)
        {
            inspectPages.add(new PageAltarRecipe(inspectRecipe));
        }

        inspectPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "inspectoris" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "inspectoris"), new EntryText(inspectPages, TextHelper.localize(keyBase + "inspectoris"), true));

        List<IPage> speedRunePages = new ArrayList<>();

        IRecipe speedRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 1));
        if (speedRecipe != null)
        {
            speedRunePages.add(BookUtils.getPageForRecipe(speedRecipe));
        }

        speedRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSpeed" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSpeed"), new EntryText(speedRunePages, TextHelper.localize(keyBase + "runeSpeed"), true));

        List<IPage> waterPages = new ArrayList<>();

        TartaricForgeRecipe waterRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_WATER.getStack());
        if (waterRecipe != null)
        {
            waterPages.add(new PageTartaricForgeRecipe(waterRecipe));
        }

        PageAlchemyArray waterRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_WATER));
        if (waterRecipePage != null)
        {
            waterPages.add(waterRecipePage);
        }

        waterPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "water" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "water"), new EntryText(waterPages, TextHelper.localize(keyBase + "water"), true));

        List<IPage> lavaPages = new ArrayList<>();

        TartaricForgeRecipe lavaRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_LAVA.getStack());
        if (lavaRecipe != null)
        {
            lavaPages.add(new PageTartaricForgeRecipe(lavaRecipe));
        }

        PageAlchemyArray lavaRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_LAVA));
        if (lavaRecipePage != null)
        {
            lavaPages.add(lavaRecipePage);
        }

        lavaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lava" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lava"), new EntryText(lavaPages, TextHelper.localize(keyBase + "lava"), true));

        List<IPage> lavaCrystalPages = new ArrayList<>();

        IRecipe lavaCrystalRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.LAVA_CRYSTAL));
        if (lavaCrystalRecipe != null)
        {
            lavaCrystalPages.add(BookUtils.getPageForRecipe(lavaCrystalRecipe));
        }

        lavaCrystalPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lavaCrystal" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lavaCrystal"), new EntryText(lavaCrystalPages, TextHelper.localize(keyBase + "lavaCrystal"), true));

        List<IPage> apprenticeorbPages = new ArrayList<>();

        AltarRecipe apprenticeorbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE));
        if (apprenticeorbRecipe != null)
        {
            apprenticeorbPages.add(new PageAltarRecipe(apprenticeorbRecipe));
        }

        apprenticeorbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "apprenticeorb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "apprenticeorb"), new EntryText(apprenticeorbPages, TextHelper.localize(keyBase + "apprenticeorb"), true));

        List<IPage> daggerPages = new ArrayList<>();

        AltarRecipe daggerOfSacrificeRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE));
        if (daggerOfSacrificeRecipe != null)
        {
            daggerPages.add(new PageAltarRecipe(daggerOfSacrificeRecipe));
        }

        daggerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "dagger" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "dagger"), new EntryText(daggerPages, TextHelper.localize(keyBase + "dagger"), true));

        List<IPage> runeSacrificePages = new ArrayList<>();

        IRecipe runeSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 3));
        if (runeSacrificeRecipe != null)
        {
            runeSacrificePages.add(BookUtils.getPageForRecipe(runeSacrificeRecipe));
        }

        runeSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSacrifice"), new EntryText(runeSacrificePages, TextHelper.localize(keyBase + "runeSacrifice"), true));

        List<IPage> runeSelfSacrificePages = new ArrayList<>();

        IRecipe runeSelfSacrificeRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 4));
        if (runeSelfSacrificeRecipe != null)
        {
            runeSelfSacrificePages.add(BookUtils.getPageForRecipe(runeSelfSacrificeRecipe));
        }

        runeSelfSacrificePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeSelfSacrifice" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeSelfSacrifice"), new EntryText(runeSelfSacrificePages, TextHelper.localize(keyBase + "runeSelfSacrifice"), true));

        List<IPage> holdingPages = new ArrayList<>();

        TartaricForgeRecipe holdingRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_HOLDING.getStack());
        if (holdingRecipe != null)
        {
            holdingPages.add(new PageTartaricForgeRecipe(holdingRecipe));
        }

        PageAlchemyArray holdingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING));
        if (holdingRecipePage != null)
        {
            holdingPages.add(holdingRecipePage);
        }

        holdingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "holding" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "holding"), new EntryText(holdingPages, TextHelper.localize(keyBase + "holding"), true));

        List<IPage> airPages = new ArrayList<>();

        TartaricForgeRecipe airRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_AIR.getStack());
        if (airRecipe != null)
        {
            airPages.add(new PageTartaricForgeRecipe(airRecipe));
        }

        PageAlchemyArray airRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_AIR));
        if (airRecipePage != null)
        {
            airPages.add(airRecipePage);
        }

        airPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "air" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "air"), new EntryText(airPages, TextHelper.localize(keyBase + "air"), true));

        List<IPage> voidPages = new ArrayList<>();

        TartaricForgeRecipe voidRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_VOID.getStack());
        if (voidRecipe != null)
        {
            voidPages.add(new PageTartaricForgeRecipe(voidRecipe));
        }

        PageAlchemyArray voidRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID));
        if (voidRecipePage != null)
        {
            voidPages.add(voidRecipePage);
        }

        voidPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "void" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "void"), new EntryText(voidPages, TextHelper.localize(keyBase + "void"), true));

        List<IPage> greenGrovePages = new ArrayList<>();

        TartaricForgeRecipe greenGroveRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_GROWTH.getStack());
        if (greenGroveRecipe != null)
        {
            greenGrovePages.add(new PageTartaricForgeRecipe(greenGroveRecipe));
        }

        PageAlchemyArray greenGroveRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE));
        if (greenGroveRecipePage != null)
        {
            greenGrovePages.add(greenGroveRecipePage);
        }

        greenGrovePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "greenGrove" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "greenGrove"), new EntryText(greenGrovePages, TextHelper.localize(keyBase + "greenGrove"), true));

        List<IPage> fastMinerPages = new ArrayList<>();

        TartaricForgeRecipe fastMinerRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_FAST_MINER.getStack());
        if (fastMinerRecipe != null)
        {
            fastMinerPages.add(new PageTartaricForgeRecipe(fastMinerRecipe));
        }

        PageAlchemyArray fastMinerRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER));
        if (fastMinerRecipePage != null)
        {
            fastMinerPages.add(fastMinerRecipePage);
        }

        fastMinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "fastMiner" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "fastMiner"), new EntryText(fastMinerPages, TextHelper.localize(keyBase + "fastMiner"), true));

        List<IPage> seerPages = new ArrayList<>();

        TartaricForgeRecipe seerRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_SIGHT.getStack());
        if (seerRecipe != null)
        {
            seerPages.add(new PageTartaricForgeRecipe(seerRecipe));
        }

        PageAlchemyArray seerRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER));
        if (seerRecipePage != null)
        {
            seerPages.add(seerRecipePage);
        }

        seerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "seer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "seer"), new EntryText(seerPages, TextHelper.localize(keyBase + "seer"), true));

        List<IPage> magicianOrbPages = new ArrayList<>();

        AltarRecipe magicianOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN));
        if (magicianOrbRecipe != null)
        {
            magicianOrbPages.add(new PageAltarRecipe(magicianOrbRecipe));
        }

        List<IPage> tier3Pages = new ArrayList<>();

        tier3Pages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "tier3" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "tier3"), new EntryText(tier3Pages, TextHelper.localize(keyBase + "tier3"), true));

        magicianOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magicianOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magicianOrb"), new EntryText(magicianOrbPages, TextHelper.localize(keyBase + "magicianOrb"), true));

        List<IPage> capacityPages = new ArrayList<>();

        IRecipe capacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 4));
        if (capacityRecipe != null)
        {
            capacityPages.add(BookUtils.getPageForRecipe(capacityRecipe));
        }

        capacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "capacity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "capacity"), new EntryText(capacityPages, TextHelper.localize(keyBase + "capacity"), true));

        List<IPage> displacementPages = new ArrayList<>();

        IRecipe displacementRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 4));
        if (displacementRecipe != null)
        {
            displacementPages.add(BookUtils.getPageForRecipe(displacementRecipe));
        }

        displacementPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "displacement" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "displacement"), new EntryText(displacementPages, TextHelper.localize(keyBase + "displacement"), true));

        List<IPage> affinityPages = new ArrayList<>();

        TartaricForgeRecipe affinityRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_AFFINITY.getStack());
        if (affinityRecipe != null)
        {
            affinityPages.add(new PageTartaricForgeRecipe(affinityRecipe));
        }

        PageAlchemyArray affinityRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY));
        if (affinityRecipePage != null)
        {
            affinityPages.add(affinityRecipePage);
        }

        affinityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "affinity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "affinity"), new EntryText(affinityPages, TextHelper.localize(keyBase + "affinity"), true));

        List<IPage> lampPages = new ArrayList<>();

        TartaricForgeRecipe lampRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_BLOOD_LIGHT.getStack());
        if (lampRecipe != null)
        {
            lampPages.add(new PageTartaricForgeRecipe(lampRecipe));
        }

        PageAlchemyArray lampRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT));
        if (lampRecipePage != null)
        {
            lampPages.add(lampRecipePage);
        }

        lampPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lamp" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lamp"), new EntryText(lampPages, TextHelper.localize(keyBase + "lamp"), true));

        List<IPage> magnetismPages = new ArrayList<>();

        TartaricForgeRecipe magnetismRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_MAGNETISM.getStack());
        if (magnetismRecipe != null)
        {
            magnetismPages.add(new PageTartaricForgeRecipe(magnetismRecipe));
        }

        PageAlchemyArray magnetismRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM));
        if (magnetismRecipePage != null)
        {
            magnetismPages.add(magnetismRecipePage);
        }

        magnetismPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magnetism" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magnetism"), new EntryText(magnetismPages, TextHelper.localize(keyBase + "magnetism"), true));

        List<IPage> peritiaPages = new ArrayList<>();

        IRecipe peritiaRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.EXPERIENCE_TOME));
        if (peritiaRecipe != null)
        {
            peritiaPages.add(BookUtils.getPageForRecipe(peritiaRecipe));
        }

        peritiaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "peritia" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "peritia"), new EntryText(peritiaPages, TextHelper.localize(keyBase + "peritia"), true));

        List<IPage> livingArmourPages = new ArrayList<>();

        TartaricForgeRecipe bindingRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_BINDING.getStack());
        if (bindingRecipe != null)
        {
            livingArmourPages.add(new PageTartaricForgeRecipe(bindingRecipe));
        }

        PageAlchemyArray bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS));
        if (bindingRecipePage != null)
        {
            livingArmourPages.add(bindingRecipePage);
        }

        livingArmourPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "livingArmour" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "livingArmour"), new EntryText(livingArmourPages, TextHelper.localize(keyBase + "livingArmour"), true));

        List<IPage> upgradePages = new ArrayList<>();

        upgradePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "upgradeTome" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "upgradeTome"), new EntryText(upgradePages, TextHelper.localize(keyBase + "upgradeTome"), true));

        List<IPage> downgradePages = new ArrayList<>();

        downgradePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "downgrade" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "downgrade"), new EntryText(downgradePages, TextHelper.localize(keyBase + "downgrade"), true));

        List<IPage> teleposerPages = new ArrayList<>();

        AltarRecipe teleposerFocusRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS));
        if (teleposerFocusRecipe != null)
        {
            teleposerPages.add(new PageAltarRecipe(teleposerFocusRecipe));
        }

        IRecipe teleposerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.TELEPOSER));
        if (teleposerRecipe != null)
        {
            teleposerPages.add(BookUtils.getPageForRecipe(teleposerRecipe));
        }

        teleposerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposer"), new EntryText(teleposerPages, TextHelper.localize(keyBase + "teleposer"), true));

        List<IPage> boundBladePages = new ArrayList<>();

        PageAlchemyArray boundBladePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SWORD));
        if (boundBladePage != null)
        {
            boundBladePages.add(boundBladePage);
        }

        boundBladePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundBlade" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundBlade"), new EntryText(boundBladePages, TextHelper.localize(keyBase + "boundBlade"), true));

        List<IPage> boundToolPages = new ArrayList<>();

        PageAlchemyArray boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_PICKAXE));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_AXE));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SHOVEL));
        if (boundToolPage != null)
        {
            boundToolPages.add(boundToolPage);
        }

        boundToolPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundTool" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundTool"), new EntryText(boundToolPages, TextHelper.localize(keyBase + "boundTool"), true));

        List<IPage> weakShardPages = new ArrayList<>();

        weakShardPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakShard" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "weakShard"), new EntryText(weakShardPages, TextHelper.localize(keyBase + "weakShard"), true));

        List<IPage> masterOrbPages = new ArrayList<>();

        AltarRecipe masterOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER));
        if (magicianOrbRecipe != null)
        {
            masterOrbPages.add(new PageAltarRecipe(masterOrbRecipe));
        }

        masterOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "masterOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "masterOrb"), new EntryText(masterOrbPages, TextHelper.localize(keyBase + "masterOrb"), true));

        List<IPage> orbRunePages = new ArrayList<>();

        IRecipe orbRuneRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 8));
        if (orbRuneRecipe != null)
        {
            orbRunePages.add(BookUtils.getPageForRecipe(orbRuneRecipe));
        }

        orbRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeOrb"), new EntryText(orbRunePages, TextHelper.localize(keyBase + "runeOrb"), true));

        List<IPage> augmentedCapacityPages = new ArrayList<>();

        IRecipe augmentedCapacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 7));
        if (orbRuneRecipe != null)
        {
            augmentedCapacityPages.add(BookUtils.getPageForRecipe(augmentedCapacityRecipe));
        }

        augmentedCapacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "augmentedCapacity" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "augmentedCapacity"), new EntryText(augmentedCapacityPages, TextHelper.localize(keyBase + "augmentedCapacity"), true));

        List<IPage> chargingPages = new ArrayList<>();

        IRecipe chargingRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 10));
        if (orbRuneRecipe != null)
        {
            chargingPages.add(BookUtils.getPageForRecipe(chargingRecipe));
        }

        chargingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "charging" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "charging"), new EntryText(chargingPages, TextHelper.localize(keyBase + "charging"), true));

        List<IPage> accelerationPages = new ArrayList<>();

        IRecipe accelerationRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 9));
        if (orbRuneRecipe != null)
        {
            accelerationPages.add(BookUtils.getPageForRecipe(accelerationRecipe));
        }

        accelerationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "acceleration" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "acceleration"), new EntryText(accelerationPages, TextHelper.localize(keyBase + "acceleration"), true));

        List<IPage> suppressionPages = new ArrayList<>();

        TartaricForgeRecipe suppressionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_SUPPRESSION.getStack());
        if (suppressionRecipe != null)
        {
            suppressionPages.add(new PageTartaricForgeRecipe(suppressionRecipe));
        }

        PageAlchemyArray suppressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SUPPRESSION));
        if (suppressionRecipePage != null)
        {
            suppressionPages.add(suppressionRecipePage);
        }

        suppressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "suppression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "suppression"), new EntryText(suppressionPages, TextHelper.localize(keyBase + "suppression"), true));

        List<IPage> hastePages = new ArrayList<>();

        TartaricForgeRecipe hasteRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_HASTE.getStack());
        if (hasteRecipe != null)
        {
            hastePages.add(new PageTartaricForgeRecipe(hasteRecipe));
        }

        PageAlchemyArray hasteRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HASTE));
        if (hasteRecipePage != null)
        {
            hastePages.add(hasteRecipePage);
        }

        hastePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "haste" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "haste"), new EntryText(hastePages, TextHelper.localize(keyBase + "haste"), true));

        List<IPage> severancePages = new ArrayList<>();

        TartaricForgeRecipe severanceRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_SEVERANCE.getStack());
        if (severanceRecipe != null)
        {
            severancePages.add(new PageTartaricForgeRecipe(severanceRecipe));
        }

        PageAlchemyArray severanceRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ENDER_SEVERANCE));
        if (severanceRecipePage != null)
        {
            severancePages.add(severanceRecipePage);
        }

        severancePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "severance" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "severance"), new EntryText(severancePages, TextHelper.localize(keyBase + "severance"), true));

        List<IPage> telepositionPages = new ArrayList<>();

        TartaricForgeRecipe telepositionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_TELEPOSITION.getStack());
        if (telepositionRecipe != null)
        {
            telepositionPages.add(new PageTartaricForgeRecipe(telepositionRecipe));
        }

        PageAlchemyArray telepositionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_TELEPOSITION));
        if (telepositionRecipePage != null)
        {
            telepositionPages.add(telepositionRecipePage);
        }

        telepositionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposition" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposition"), new EntryText(telepositionPages, TextHelper.localize(keyBase + "teleposition"), true));

        List<IPage> compressionPages = new ArrayList<>();

        TartaricForgeRecipe compressionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_COMPRESSION.getStack());
        if (compressionRecipe != null)
        {
            compressionPages.add(new PageTartaricForgeRecipe(compressionRecipe));
        }

        PageAlchemyArray compressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_COMPRESSION));
        if (compressionRecipePage != null)
        {
            compressionPages.add(compressionRecipePage);
        }

        compressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "compression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "compression"), new EntryText(compressionPages, TextHelper.localize(keyBase + "compression"), true));

        List<IPage> bridgePages = new ArrayList<>();

        TartaricForgeRecipe bridgeRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentTypes.REAGENT_BRIDGE.getStack());
        if (bridgeRecipe != null)
        {
            bridgePages.add(new PageTartaricForgeRecipe(bridgeRecipe));
        }

        PageAlchemyArray bridgeRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_PHANTOM_BRIDGE));
        if (bridgeRecipePage != null)
        {
            bridgePages.add(bridgeRecipePage);
        }

        bridgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bridge" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "bridge"), new EntryText(bridgePages, TextHelper.localize(keyBase + "bridge"), true));

        List<IPage> mimicPages = new ArrayList<>();

        IRecipe mimicRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.MIMIC, 1, 1));
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
