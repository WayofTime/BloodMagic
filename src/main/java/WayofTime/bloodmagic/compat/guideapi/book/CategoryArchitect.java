package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ComponentType;
import WayofTime.bloodmagic.util.helper.RecipeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageJsonRecipe;
import amerifrance.guideapi.page.PageText;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CategoryArchitect {

    public static void buildCategory(Book book) {
        final String keyBase = "guide." + BloodMagic.MODID + ".entry.architect.";
        CategoryItemStack category = new CategoryItemStack(keyBase + "architect", new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION));
        category.withKeyBase(BloodMagic.MODID);

        category.addEntry("intro", new EntryText(keyBase + "intro", true));
        category.getEntry("intro").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "intro.info"), 370));

        category.addEntry("bloodaltar", new EntryText(keyBase + "bloodaltar", true));
        category.getEntry("bloodaltar").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "altar")));
        category.getEntry("bloodaltar").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodaltar.info.1"), 370));
        category.getEntry("bloodaltar").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "sacrificial_dagger")));
        category.getEntry("bloodaltar").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodaltar.info.2"), 370));

        category.addEntry("ash", new EntryText(keyBase + "ash", true));
        category.getEntry("ash").addPage(BookUtils.getForgeRecipe(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES)));
        category.getEntry("ash").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "ash.info"), 370));

        category.addEntry("divination", new EntryText(keyBase + "divination", true));
        category.getEntry("divination").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION)));
        category.getEntry("divination").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "divination.info")));

        category.addEntry("soulnetwork", new EntryText(keyBase + "soulnetwork", true));
        category.getEntry("soulnetwork").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "soulnetwork.info")));

        category.addEntry("weakorb", new EntryText(keyBase + "weakorb", true));
        category.getEntry("weakorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "weakorb.info1"), 370));
        category.getEntry("weakorb").addPage(BookUtils.getAltarPage(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_WEAK)));
        category.getEntry("weakorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "weakorb.info2"), 370));

        category.addEntry("incense", new EntryText(keyBase + "incense", true));
        category.getEntry("incense").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "incense_altar")));
        category.getEntry("incense").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "incense.info.1"), 370));
        category.getEntry("incense").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "path_wood")));
        category.getEntry("incense").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "incense.info.2"), 370));

        category.addEntry("bloodrune", new EntryText(keyBase + "bloodrune", true));
        category.getEntry("bloodrune").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "blood_rune_blank")));
        category.getEntry("bloodrune").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodrune.info.1"), 370));

        category.addEntry("inspectoris", new EntryText(keyBase + "inspectoris", true));
        category.getEntry("inspectoris").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK)));
        category.getEntry("inspectoris").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "inspectoris.info.1"), 370));

        category.addEntry("runeSpeed", new EntryText(keyBase + "runeSpeed", true));
        category.getEntry("runeSpeed").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 1)));
        category.getEntry("runeSpeed").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSpeed.info.1"), 370));

        category.addEntry("water", new EntryText(keyBase + "water", true));
        category.getEntry("water").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_WATER.getStack()));
        category.getEntry("water").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_WATER)));
        category.getEntry("water").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "water.info.1"), 370));

        category.addEntry("lava", new EntryText(keyBase + "lava", true));
        category.getEntry("lava").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_LAVA.getStack()));
        category.getEntry("lava").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_LAVA)));
        category.getEntry("lava").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lava.info.1"), 370));

        category.addEntry("lavaCrystal", new EntryText(keyBase + "lavaCrystal", true));
        category.getEntry("lavaCrystal").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "lava_crystal")));
        category.getEntry("lavaCrystal").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lavaCrystal.info.1"), 370));

        category.addEntry("apprenticeorb", new EntryText(keyBase + "apprenticeorb", true));
        category.getEntry("apprenticeorb").addPage(BookUtils.getAltarPage(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE)));
        category.getEntry("apprenticeorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "apprenticeorb.info.1"), 370));

        category.addEntry("dagger", new EntryText(keyBase + "dagger", true));
        category.getEntry("dagger").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE)));
        category.getEntry("dagger").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "dagger.info.1"), 370));

        category.addEntry("runeSacrifice", new EntryText(keyBase + "runeSacrifice", true));
        category.getEntry("runeSacrifice").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "blood_rune_sacrifice")));
        category.getEntry("runeSacrifice").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSacrifice.info.1"), 370));

        category.addEntry("runeSelfSacrifice", new EntryText(keyBase + "runeSelfSacrifice", true));
        category.getEntry("runeSelfSacrifice").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "blood_rune_selfsacrifice")));
        category.getEntry("runeSelfSacrifice").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSelfSacrifice.info.1"), 370));

        category.addEntry("holding", new EntryText(keyBase + "holding", true));
        category.getEntry("holding").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_HOLDING.getStack()));
        category.getEntry("holding").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING)));
        category.getEntry("holding").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "holding.info.1"), 370));

        category.addEntry("air", new EntryText(keyBase + "air", true));
        category.getEntry("air").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_AIR.getStack()));
        category.getEntry("air").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_AIR)));
        category.getEntry("air").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "air.info.1"), 370));

        category.addEntry("void", new EntryText(keyBase + "void", true));
        category.getEntry("void").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_VOID.getStack()));
        category.getEntry("void").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID)));
        category.getEntry("void").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "void.info.1"), 370));

        category.addEntry("greenGrove", new EntryText(keyBase + "greenGrove", true));
        category.getEntry("greenGrove").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_GROWTH.getStack()));
        category.getEntry("greenGrove").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE)));
        category.getEntry("greenGrove").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "greenGrove.info.1"), 370));

        category.addEntry("fastMiner", new EntryText(keyBase + "fastMiner", true));
        category.getEntry("fastMiner").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_FASTMINER.getStack()));
        category.getEntry("fastMiner").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER)));
        category.getEntry("fastMiner").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "fastMiner.info.1"), 370));

        category.addEntry("seer", new EntryText(keyBase + "seer", true));
        category.getEntry("seer").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_SIGHT.getStack()));
        category.getEntry("seer").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER)));
        category.getEntry("seer").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "seer.info.1"), 370));

        category.addEntry("magicianOrb", new EntryText(keyBase + "magicianOrb", true));
        category.getEntry("magicianOrb").addPage(BookUtils.getAltarPage(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN)));
        category.getEntry("magicianOrb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "magicianOrb.info.1"), 370));

        category.addEntry("capacity", new EntryText(keyBase + "capacity", true));
        category.getEntry("capacity").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "blood_rune_capacity")));
        category.getEntry("capacity").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "capacity.info.1"), 370));

        category.addEntry("displacement", new EntryText(keyBase + "displacement", true));
        category.getEntry("displacement").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "blood_rune_displacement")));
        category.getEntry("displacement").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "displacement.info.1"), 370));

        category.addEntry("affinity", new EntryText(keyBase + "affinity", true));
        category.getEntry("affinity").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_AFFINITY.getStack()));
        category.getEntry("affinity").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY)));
        category.getEntry("affinity").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "affinity.info"), 370));

        category.addEntry("lamp", new EntryText(keyBase + "lamp", true));
        category.getEntry("lamp").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_BLOODLIGHT.getStack()));
        category.getEntry("lamp").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT)));
        category.getEntry("lamp").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lamp.info.1"), 370));

        category.addEntry("magnetism", new EntryText(keyBase + "magnetism", true));
        category.getEntry("magnetism").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_MAGNETISM.getStack()));
        category.getEntry("magnetism").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM)));
        category.getEntry("magnetism").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "magnetism.info.1"), 370));

        category.addEntry("peritia", new EntryText(keyBase + "peritia", true));
        category.getEntry("peritia").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "experience_tome")));
        category.getEntry("peritia").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "peritia.info.1"), 370));

        category.addEntry("livingArmour", new EntryText(keyBase + "livingArmour", true));
        category.getEntry("livingArmour").addPage(BookUtils.getForgeRecipe(ComponentType.REAGENT_BINDING.getStack()));
        category.getEntry("livingArmour").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST)));
        category.getEntry("livingArmour").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET)));
        category.getEntry("livingArmour").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS)));
        category.getEntry("livingArmour").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS)));
        category.getEntry("livingArmour").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "livingArmour.info.1"), 370));

        category.addEntry("upgradeTome", new EntryText(keyBase + "upgradeTome", true));
        category.getEntry("upgradeTome").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "upgradeTome.info.1")));

        category.addEntry("downgrade", new EntryText(keyBase + "downgrade", true));
        category.getEntry("downgrade").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "downgrade.info.1")));

        category.addEntry("teleposer", new EntryText(keyBase + "teleposer", true));
        category.getEntry("teleposer").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)));
        category.getEntry("teleposer").addPage(new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, "teleposer")));
        category.getEntry("teleposer").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "teleposer.info.1"), 370));

        List<IPage> boundBladePages = new ArrayList<IPage>();

        PageAlchemyArray boundBladePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SWORD));
        if (boundBladePage != null) {
            boundBladePages.add(boundBladePage);
        }

        boundBladePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundBlade" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundBlade"), new EntryText(boundBladePages, TextHelper.localize(keyBase + "boundBlade"), true));

        List<IPage> boundToolPages = new ArrayList<IPage>();

        PageAlchemyArray boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_PICKAXE));
        if (boundToolPage != null) {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_AXE));
        if (boundToolPage != null) {
            boundToolPages.add(boundToolPage);
        }

        boundToolPage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SHOVEL));
        if (boundToolPage != null) {
            boundToolPages.add(boundToolPage);
        }

        boundToolPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "boundTool" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "boundTool"), new EntryText(boundToolPages, TextHelper.localize(keyBase + "boundTool"), true));

        List<IPage> weakShardPages = new ArrayList<IPage>();

        weakShardPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "weakShard" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "weakShard"), new EntryText(weakShardPages, TextHelper.localize(keyBase + "weakShard"), true));

        List<IPage> masterOrbPages = new ArrayList<IPage>();

        AltarRecipe masterOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER));
        if (magicianOrbRecipe != null) {
            masterOrbPages.add(new PageAltarRecipe(masterOrbRecipe));
        }

        masterOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "masterOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "masterOrb"), new EntryText(masterOrbPages, TextHelper.localize(keyBase + "masterOrb"), true));

        List<IPage> orbRunePages = new ArrayList<IPage>();

        IRecipe orbRuneRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 8));
        if (orbRuneRecipe != null) {
            orbRunePages.add(BookUtils.getPageForRecipe(orbRuneRecipe));
        }

        orbRunePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "runeOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "runeOrb"), new EntryText(orbRunePages, TextHelper.localize(keyBase + "runeOrb"), true));

        List<IPage> augmentedCapacityPages = new ArrayList<IPage>();

        IRecipe augmentedCapacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 7));
        if (orbRuneRecipe != null) {
            augmentedCapacityPages.add(BookUtils.getPageForRecipe(augmentedCapacityRecipe));
        }

        augmentedCapacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "augmentedCapacity" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "augmentedCapacity"), new EntryText(augmentedCapacityPages, TextHelper.localize(keyBase + "augmentedCapacity"), true));

        List<IPage> chargingPages = new ArrayList<IPage>();

        IRecipe chargingRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 10));
        if (orbRuneRecipe != null) {
            chargingPages.add(BookUtils.getPageForRecipe(chargingRecipe));
        }

        chargingPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "charging" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "charging"), new EntryText(chargingPages, TextHelper.localize(keyBase + "charging"), true));

        List<IPage> accelerationPages = new ArrayList<IPage>();

        IRecipe accelerationRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 9));
        if (orbRuneRecipe != null) {
            accelerationPages.add(BookUtils.getPageForRecipe(accelerationRecipe));
        }

        accelerationPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "acceleration" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "acceleration"), new EntryText(accelerationPages, TextHelper.localize(keyBase + "acceleration"), true));

        List<IPage> suppressionPages = new ArrayList<IPage>();

        TartaricForgeRecipe suppressionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_SUPPRESSION.getStack());
        if (suppressionRecipe != null) {
            suppressionPages.add(new PageTartaricForgeRecipe(suppressionRecipe));
        }

        PageAlchemyArray suppressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SUPPRESSION));
        if (suppressionRecipePage != null) {
            suppressionPages.add(suppressionRecipePage);
        }

        suppressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "suppression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "suppression"), new EntryText(suppressionPages, TextHelper.localize(keyBase + "suppression"), true));

        List<IPage> hastePages = new ArrayList<IPage>();

        TartaricForgeRecipe hasteRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_HASTE.getStack());
        if (hasteRecipe != null) {
            hastePages.add(new PageTartaricForgeRecipe(hasteRecipe));
        }

        PageAlchemyArray hasteRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HASTE));
        if (hasteRecipePage != null) {
            hastePages.add(hasteRecipePage);
        }

        hastePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "haste" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "haste"), new EntryText(hastePages, TextHelper.localize(keyBase + "haste"), true));

        List<IPage> severancePages = new ArrayList<IPage>();

        TartaricForgeRecipe severanceRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_SEVERANCE.getStack());
        if (severanceRecipe != null) {
            severancePages.add(new PageTartaricForgeRecipe(severanceRecipe));
        }

        PageAlchemyArray severanceRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ENDER_SEVERANCE));
        if (severanceRecipePage != null) {
            severancePages.add(severanceRecipePage);
        }

        severancePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "severance" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "severance"), new EntryText(severancePages, TextHelper.localize(keyBase + "severance"), true));

        List<IPage> telepositionPages = new ArrayList<IPage>();

        TartaricForgeRecipe telepositionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_TELEPOSITION.getStack());
        if (telepositionRecipe != null) {
            telepositionPages.add(new PageTartaricForgeRecipe(telepositionRecipe));
        }

        PageAlchemyArray telepositionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_TELEPOSITION));
        if (telepositionRecipePage != null) {
            telepositionPages.add(telepositionRecipePage);
        }

        telepositionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposition" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposition"), new EntryText(telepositionPages, TextHelper.localize(keyBase + "teleposition"), true));

        List<IPage> compressionPages = new ArrayList<IPage>();

        TartaricForgeRecipe compressionRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_COMPRESSION.getStack());
        if (compressionRecipe != null) {
            compressionPages.add(new PageTartaricForgeRecipe(compressionRecipe));
        }

        PageAlchemyArray compressionRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_COMPRESSION));
        if (compressionRecipePage != null) {
            compressionPages.add(compressionRecipePage);
        }

        compressionPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "compression" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "compression"), new EntryText(compressionPages, TextHelper.localize(keyBase + "compression"), true));

        List<IPage> bridgePages = new ArrayList<IPage>();

        TartaricForgeRecipe bridgeRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_BRIDGE.getStack());
        if (bridgeRecipe != null) {
            bridgePages.add(new PageTartaricForgeRecipe(bridgeRecipe));
        }

        PageAlchemyArray bridgeRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_PHANTOM_BRIDGE));
        if (bridgeRecipePage != null) {
            bridgePages.add(bridgeRecipePage);
        }

        bridgePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "bridge" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "bridge"), new EntryText(bridgePages, TextHelper.localize(keyBase + "bridge"), true));

        List<IPage> mimicPages = new ArrayList<IPage>();

        IRecipe mimicRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.MIMIC, 1, 1));
        if (mimicRecipe != null) {
            mimicPages.add(BookUtils.getPageForRecipe(mimicRecipe));
        }

        mimicPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "mimic" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "mimic"), new EntryText(mimicPages, TextHelper.localize(keyBase + "mimic"), true));

        category.entries.values().forEach(e -> e.pageList.stream().filter(p -> p instanceof PageText).forEach(p -> ((PageText) p).setUnicodeFlag(true)));

        book.addCategory(category);
    }
}
