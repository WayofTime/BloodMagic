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

        List<IPage> voidPages = new ArrayList<IPage>();

        TartaricForgeRecipe voidRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_VOID.getStack());
        if (voidRecipe != null) {
            voidPages.add(new PageTartaricForgeRecipe(voidRecipe));
        }

        PageAlchemyArray voidRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID));
        if (voidRecipePage != null) {
            voidPages.add(voidRecipePage);
        }

        voidPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "void" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "void"), new EntryText(voidPages, TextHelper.localize(keyBase + "void"), true));

        List<IPage> greenGrovePages = new ArrayList<IPage>();

        TartaricForgeRecipe greenGroveRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_GROWTH.getStack());
        if (greenGroveRecipe != null) {
            greenGrovePages.add(new PageTartaricForgeRecipe(greenGroveRecipe));
        }

        PageAlchemyArray greenGroveRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE));
        if (greenGroveRecipePage != null) {
            greenGrovePages.add(greenGroveRecipePage);
        }

        greenGrovePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "greenGrove" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "greenGrove"), new EntryText(greenGrovePages, TextHelper.localize(keyBase + "greenGrove"), true));

        List<IPage> fastMinerPages = new ArrayList<IPage>();

        TartaricForgeRecipe fastMinerRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_FASTMINER.getStack());
        if (fastMinerRecipe != null) {
            fastMinerPages.add(new PageTartaricForgeRecipe(fastMinerRecipe));
        }

        PageAlchemyArray fastMinerRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER));
        if (fastMinerRecipePage != null) {
            fastMinerPages.add(fastMinerRecipePage);
        }

        fastMinerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "fastMiner" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "fastMiner"), new EntryText(fastMinerPages, TextHelper.localize(keyBase + "fastMiner"), true));

        List<IPage> seerPages = new ArrayList<IPage>();

        TartaricForgeRecipe seerRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_SIGHT.getStack());
        if (seerRecipe != null) {
            seerPages.add(new PageTartaricForgeRecipe(seerRecipe));
        }

        PageAlchemyArray seerRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER));
        if (seerRecipePage != null) {
            seerPages.add(seerRecipePage);
        }

        seerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "seer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "seer"), new EntryText(seerPages, TextHelper.localize(keyBase + "seer"), true));

        List<IPage> magicianOrbPages = new ArrayList<IPage>();

        AltarRecipe magicianOrbRecipe = RecipeHelper.getAltarRecipeForOutput(OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN));
        if (magicianOrbRecipe != null) {
            magicianOrbPages.add(new PageAltarRecipe(magicianOrbRecipe));
        }

        magicianOrbPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magicianOrb" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magicianOrb"), new EntryText(magicianOrbPages, TextHelper.localize(keyBase + "magicianOrb"), true));

        List<IPage> capacityPages = new ArrayList<IPage>();

        IRecipe capacityRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 4));
        if (capacityRecipe != null) {
            capacityPages.add(BookUtils.getPageForRecipe(capacityRecipe));
        }

        capacityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "capacity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "capacity"), new EntryText(capacityPages, TextHelper.localize(keyBase + "capacity"), true));

        List<IPage> displacementPages = new ArrayList<IPage>();

        IRecipe displacementRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 4));
        if (displacementRecipe != null) {
            displacementPages.add(BookUtils.getPageForRecipe(displacementRecipe));
        }

        displacementPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "displacement" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "displacement"), new EntryText(displacementPages, TextHelper.localize(keyBase + "displacement"), true));

        List<IPage> affinityPages = new ArrayList<IPage>();

        TartaricForgeRecipe affinityRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_AFFINITY.getStack());
        if (affinityRecipe != null) {
            affinityPages.add(new PageTartaricForgeRecipe(affinityRecipe));
        }

        PageAlchemyArray affinityRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY));
        if (affinityRecipePage != null) {
            affinityPages.add(affinityRecipePage);
        }

        affinityPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "affinity" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "affinity"), new EntryText(affinityPages, TextHelper.localize(keyBase + "affinity"), true));

        List<IPage> lampPages = new ArrayList<IPage>();

        TartaricForgeRecipe lampRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_BLOODLIGHT.getStack());
        if (lampRecipe != null) {
            lampPages.add(new PageTartaricForgeRecipe(lampRecipe));
        }

        PageAlchemyArray lampRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT));
        if (lampRecipePage != null) {
            lampPages.add(lampRecipePage);
        }

        lampPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "lamp" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "lamp"), new EntryText(lampPages, TextHelper.localize(keyBase + "lamp"), true));

        List<IPage> magnetismPages = new ArrayList<IPage>();

        TartaricForgeRecipe magnetismRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_MAGNETISM.getStack());
        if (magnetismRecipe != null) {
            magnetismPages.add(new PageTartaricForgeRecipe(magnetismRecipe));
        }

        PageAlchemyArray magnetismRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM));
        if (magnetismRecipePage != null) {
            magnetismPages.add(magnetismRecipePage);
        }

        magnetismPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "magnetism" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "magnetism"), new EntryText(magnetismPages, TextHelper.localize(keyBase + "magnetism"), true));

        List<IPage> peritiaPages = new ArrayList<IPage>();

        IRecipe peritiaRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.EXPERIENCE_TOME));
        if (peritiaRecipe != null) {
            peritiaPages.add(BookUtils.getPageForRecipe(peritiaRecipe));
        }

        peritiaPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "peritia" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "peritia"), new EntryText(peritiaPages, TextHelper.localize(keyBase + "peritia"), true));

        List<IPage> livingArmourPages = new ArrayList<IPage>();

        TartaricForgeRecipe bindingRecipe = RecipeHelper.getForgeRecipeForOutput(ComponentType.REAGENT_BINDING.getStack());
        if (bindingRecipe != null) {
            livingArmourPages.add(new PageTartaricForgeRecipe(bindingRecipe));
        }

        PageAlchemyArray bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST));
        if (bindingRecipePage != null) {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET));
        if (bindingRecipePage != null) {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS));
        if (bindingRecipePage != null) {
            livingArmourPages.add(bindingRecipePage);
        }

        bindingRecipePage = BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS));
        if (bindingRecipePage != null) {
            livingArmourPages.add(bindingRecipePage);
        }

        livingArmourPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "livingArmour" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "livingArmour"), new EntryText(livingArmourPages, TextHelper.localize(keyBase + "livingArmour"), true));

        List<IPage> upgradePages = new ArrayList<IPage>();

        upgradePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "upgradeTome" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "upgradeTome"), new EntryText(upgradePages, TextHelper.localize(keyBase + "upgradeTome"), true));

        List<IPage> downgradePages = new ArrayList<IPage>();

        downgradePages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "downgrade" + ".info"), 370));
        entries.put(new ResourceLocation(keyBase + "downgrade"), new EntryText(downgradePages, TextHelper.localize(keyBase + "downgrade"), true));

        List<IPage> teleposerPages = new ArrayList<IPage>();

        AltarRecipe teleposerFocusRecipe = RecipeHelper.getAltarRecipeForOutput(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS));
        if (teleposerFocusRecipe != null) {
            teleposerPages.add(new PageAltarRecipe(teleposerFocusRecipe));
        }

        IRecipe teleposerRecipe = RecipeHelper.getRecipeForOutput(new ItemStack(RegistrarBloodMagicBlocks.TELEPOSER));
        if (teleposerRecipe != null) {
            teleposerPages.add(BookUtils.getPageForRecipe(teleposerRecipe));
        }

        teleposerPages.addAll(PageHelper.pagesForLongText(TextHelper.localize(keyBase + "teleposer" + ".info.1"), 370));
        entries.put(new ResourceLocation(keyBase + "teleposer"), new EntryText(teleposerPages, TextHelper.localize(keyBase + "teleposer"), true));

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
