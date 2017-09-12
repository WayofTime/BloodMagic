package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ComponentType;
import WayofTime.bloodmagic.item.types.ReagentType;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CategoryArchitect {

    public static void buildCategory(Book book) {
        final String keyBase = "guide." + BloodMagic.MODID + ".entry.architect.";

        CategoryItemStack category = new CategoryItemStack(keyBase + "architect", new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION));
        category.withKeyBase(BloodMagic.MODID);

        category.addEntry("intro", new EntryText(keyBase + "intro", true));
        category.getEntry("intro").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "intro.info"), 370));

        category.addEntry("bloodaltar", new EntryText(keyBase + "bloodaltar", true));
        category.getEntry("bloodaltar").addPage(BookUtils.getCraftingPage("altar"));
        category.getEntry("bloodaltar").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodaltar.info.1"), 370));
        category.getEntry("bloodaltar").addPage(BookUtils.getCraftingPage("sacrificial_dagger"));
        category.getEntry("bloodaltar").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodaltar.info.2"), 370));

        category.addEntry("ash", new EntryText(keyBase + "ash", true));
        category.getEntry("ash").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES)));
        category.getEntry("ash").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "ash.info"), 370));

        category.addEntry("divination", new EntryText(keyBase + "divination", true));
        category.getEntry("divination").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION)));
        category.getEntry("divination").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "divination.info")));

        category.addEntry("soulnetwork", new EntryText(keyBase + "soulnetwork", true));
        category.getEntry("soulnetwork").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "soulnetwork.info")));

        category.addEntry("weakorb", new EntryText(keyBase + "weakorb", true));
        category.getEntry("weakorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "weakorb.info1"), 370));
        category.getEntry("weakorb").addPage(BookUtils.getAltarPage(getOrbStack(RegistrarBloodMagic.ORB_WEAK)));
        category.getEntry("weakorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "weakorb.info2"), 370));

        category.addEntry("incense", new EntryText(keyBase + "incense", true));
        category.getEntry("incense").addPage(BookUtils.getCraftingPage("incense_altar"));
        category.getEntry("incense").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "incense.info.1"), 370));
        category.getEntry("incense").addPage(BookUtils.getCraftingPage("path_wood"));
        category.getEntry("incense").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "incense.info.2"), 370));

        category.addEntry("bloodrune", new EntryText(keyBase + "bloodrune", true));
        category.getEntry("bloodrune").addPage(BookUtils.getCraftingPage("blood_rune_blank"));
        category.getEntry("bloodrune").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bloodrune.info.1"), 370));

        category.addEntry("inspectoris", new EntryText(keyBase + "inspectoris", true));
        category.getEntry("inspectoris").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK)));
        category.getEntry("inspectoris").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "inspectoris.info.1"), 370));

        category.addEntry("runeSpeed", new EntryText(keyBase + "runeSpeed", true));
        category.getEntry("runeSpeed").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_RUNE, 1, 1)));
        category.getEntry("runeSpeed").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSpeed.info.1"), 370));

        category.addEntry("water", new EntryText(keyBase + "water", true));
        category.getEntry("water").addPage(BookUtils.getForgePage(ReagentType.REAGENT_WATER.getStack()));
        category.getEntry("water").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_WATER)));
        category.getEntry("water").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "water.info.1"), 370));

        category.addEntry("lava", new EntryText(keyBase + "lava", true));
        category.getEntry("lava").addPage(BookUtils.getForgePage(ReagentType.REAGENT_LAVA.getStack()));
        category.getEntry("lava").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_LAVA)));
        category.getEntry("lava").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lava.info.1"), 370));

        category.addEntry("lavaCrystal", new EntryText(keyBase + "lavaCrystal", true));
        category.getEntry("lavaCrystal").addPage(BookUtils.getCraftingPage("lava_crystal"));
        category.getEntry("lavaCrystal").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lavaCrystal.info.1"), 370));

        category.addEntry("apprenticeorb", new EntryText(keyBase + "apprenticeorb", true));
        category.getEntry("apprenticeorb").addPage(BookUtils.getAltarPage(getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE)));
        category.getEntry("apprenticeorb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "apprenticeorb.info.1"), 370));

        category.addEntry("dagger", new EntryText(keyBase + "dagger", true));
        category.getEntry("dagger").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE)));
        category.getEntry("dagger").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "dagger.info.1"), 370));

        category.addEntry("runeSacrifice", new EntryText(keyBase + "runeSacrifice", true));
        category.getEntry("runeSacrifice").addPage(BookUtils.getCraftingPage("blood_rune_sacrifice"));
        category.getEntry("runeSacrifice").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSacrifice.info.1"), 370));

        category.addEntry("runeSelfSacrifice", new EntryText(keyBase + "runeSelfSacrifice", true));
        category.getEntry("runeSelfSacrifice").addPage(BookUtils.getCraftingPage("blood_rune_selfsacrifice"));
        category.getEntry("runeSelfSacrifice").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeSelfSacrifice.info.1"), 370));

        category.addEntry("holding", new EntryText(keyBase + "holding", true));
        category.getEntry("holding").addPage(BookUtils.getForgePage(ReagentType.REAGENT_HOLDING.getStack()));
        category.getEntry("holding").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING)));
        category.getEntry("holding").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "holding.info.1"), 370));

        category.addEntry("air", new EntryText(keyBase + "air", true));
        category.getEntry("air").addPage(BookUtils.getForgePage(ReagentType.REAGENT_AIR.getStack()));
        category.getEntry("air").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_AIR)));
        category.getEntry("air").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "air.info.1"), 370));

        category.addEntry("void", new EntryText(keyBase + "void", true));
        category.getEntry("void").addPage(BookUtils.getForgePage(ReagentType.REAGENT_VOID.getStack()));
        category.getEntry("void").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID)));
        category.getEntry("void").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "void.info.1"), 370));

        category.addEntry("greenGrove", new EntryText(keyBase + "greenGrove", true));
        category.getEntry("greenGrove").addPage(BookUtils.getForgePage(ReagentType.REAGENT_GROWTH.getStack()));
        category.getEntry("greenGrove").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE)));
        category.getEntry("greenGrove").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "greenGrove.info.1"), 370));

        category.addEntry("fastMiner", new EntryText(keyBase + "fastMiner", true));
        category.getEntry("fastMiner").addPage(BookUtils.getForgePage(ReagentType.REAGENT_FASTMINER.getStack()));
        category.getEntry("fastMiner").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER)));
        category.getEntry("fastMiner").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "fastMiner.info.1"), 370));

        category.addEntry("seer", new EntryText(keyBase + "seer", true));
        category.getEntry("seer").addPage(BookUtils.getForgePage(ReagentType.REAGENT_SIGHT.getStack()));
        category.getEntry("seer").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER)));
        category.getEntry("seer").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "seer.info.1"), 370));

        category.addEntry("magicianOrb", new EntryText(keyBase + "magicianOrb", true));
        category.getEntry("magicianOrb").addPage(BookUtils.getAltarPage(getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN)));
        category.getEntry("magicianOrb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "magicianOrb.info.1"), 370));

        category.addEntry("capacity", new EntryText(keyBase + "capacity", true));
        category.getEntry("capacity").addPage(BookUtils.getCraftingPage("blood_rune_capacity"));
        category.getEntry("capacity").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "capacity.info.1"), 370));

        category.addEntry("displacement", new EntryText(keyBase + "displacement", true));
        category.getEntry("displacement").addPage(BookUtils.getCraftingPage("blood_rune_displacement"));
        category.getEntry("displacement").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "displacement.info.1"), 370));

        category.addEntry("affinity", new EntryText(keyBase + "affinity", true));
        category.getEntry("affinity").addPage(BookUtils.getForgePage(ReagentType.REAGENT_AFFINITY.getStack()));
        category.getEntry("affinity").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY)));
        category.getEntry("affinity").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "affinity.info"), 370));

        category.addEntry("lamp", new EntryText(keyBase + "lamp", true));
        category.getEntry("lamp").addPage(BookUtils.getForgePage(ReagentType.REAGENT_BLOODLIGHT.getStack()));
        category.getEntry("lamp").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT)));
        category.getEntry("lamp").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lamp.info.1"), 370));

        category.addEntry("magnetism", new EntryText(keyBase + "magnetism", true));
        category.getEntry("magnetism").addPage(BookUtils.getForgePage(ReagentType.REAGENT_MAGNETISM.getStack()));
        category.getEntry("magnetism").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM)));
        category.getEntry("magnetism").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "magnetism.info.1"), 370));

        category.addEntry("peritia", new EntryText(keyBase + "peritia", true));
        category.getEntry("peritia").addPage(BookUtils.getCraftingPage("experience_tome"));
        category.getEntry("peritia").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "peritia.info.1"), 370));

        category.addEntry("livingArmour", new EntryText(keyBase + "livingArmour", true));
        category.getEntry("livingArmour").addPage(BookUtils.getForgePage(ReagentType.REAGENT_BINDING.getStack()));
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
        category.getEntry("teleposer").addPage(BookUtils.getCraftingPage("teleposer"));
        category.getEntry("teleposer").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "teleposer.info.1"), 370));

        category.addEntry("boundBlade", new EntryText(keyBase + "boundBlade", true));
        category.getEntry("boundBlade").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SWORD)));
        category.getEntry("boundBlade").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "boundBlade.info.1"), 370));

        category.addEntry("boundTool", new EntryText(keyBase + "boundTool", true));
        category.getEntry("boundTool").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_PICKAXE)));
        category.getEntry("boundTool").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_AXE)));
        category.getEntry("boundTool").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.BOUND_SHOVEL)));
        category.getEntry("boundTool").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "boundTool.info.1"), 370));
        
        category.addEntry("weakShard", new EntryText(keyBase + "weakShard", true));
        category.getEntry("weakShard").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "weakShard.info.1"), 370));
        
        category.addEntry("masterOrb", new EntryText(keyBase + "masterOrb", true));
        category.getEntry("masterOrb").addPage(BookUtils.getAltarPage(getOrbStack(RegistrarBloodMagic.ORB_MASTER)));
        category.getEntry("masterOrb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "masterOrb.info.1"), 370));
        
        category.addEntry("runeOrb", new EntryText(keyBase + "runeOrb", true));
        category.getEntry("runeOrb").addPage(BookUtils.getCraftingPage("blood_rune_orb"));
        category.getEntry("runeOrb").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "runeOrb.info.1"), 370));
        
        category.addEntry("augmentedCapacity", new EntryText(keyBase + "augmentedCapacity", true));
        category.getEntry("augmentedCapacity").addPage(BookUtils.getCraftingPage("blood_rune_augcapacity"));
        category.getEntry("augmentedCapacity").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "augmentedCapacity.info.1"), 370));

        category.addEntry("charging", new EntryText(keyBase + "charging", true));
        category.getEntry("charging").addPage(BookUtils.getCraftingPage("blood_rune_charging"));
        category.getEntry("charging").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "charging.info.1"), 370));

        category.addEntry("acceleration", new EntryText(keyBase + "acceleration", true));
        category.getEntry("acceleration").addPage(BookUtils.getCraftingPage("blood_rune_acceleration"));
        category.getEntry("acceleration").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "acceleration.info.1"), 370));

        category.addEntry("suppression", new EntryText(keyBase + "suppression", true));
        category.getEntry("suppression").addPage(BookUtils.getForgePage(ReagentType.REAGENT_SUPPRESSION.getStack()));
        category.getEntry("suppression").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_SUPPRESSION)));
        category.getEntry("suppression").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "suppression.info.1"), 370));

        category.addEntry("haste", new EntryText(keyBase + "haste", true));
        category.getEntry("haste").addPage(BookUtils.getForgePage(ReagentType.REAGENT_HASTE.getStack()));
        category.getEntry("haste").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_HASTE)));
        category.getEntry("haste").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "haste.info.1"), 370));

        category.addEntry("severance", new EntryText(keyBase + "severance", true));
        category.getEntry("severance").addPage(BookUtils.getForgePage(ReagentType.REAGENT_SEVERANCE.getStack()));
        category.getEntry("severance").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_ENDER_SEVERANCE)));
        category.getEntry("severance").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "severance.info.1"), 370));

        category.addEntry("teleposition", new EntryText(keyBase + "teleposition", true));
        category.getEntry("teleposition").addPage(BookUtils.getForgePage(ReagentType.REAGENT_TELEPOSITION.getStack()));
        category.getEntry("teleposition").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_TELEPOSITION)));
        category.getEntry("teleposition").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "teleposition.info.1"), 370));

        category.addEntry("compression", new EntryText(keyBase + "compression", true));
        category.getEntry("compression").addPage(BookUtils.getForgePage(ReagentType.REAGENT_COMPRESSION.getStack()));
        category.getEntry("compression").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_COMPRESSION)));
        category.getEntry("compression").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "compression.info.1"), 370));

        category.addEntry("bridge", new EntryText(keyBase + "bridge", true));
        category.getEntry("bridge").addPage(BookUtils.getForgePage(ReagentType.REAGENT_BRIDGE.getStack()));
        category.getEntry("bridge").addPage(BookUtils.getAlchemyPage(new ItemStack(RegistrarBloodMagicItems.SIGIL_PHANTOM_BRIDGE)));
        category.getEntry("bridge").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bridge.info.1"), 370));

        category.addEntry("mimic", new EntryText(keyBase + "mimic", true));
        category.getEntry("mimic").addPage(BookUtils.getCraftingPage("mimic_solidopaque"));
        category.getEntry("mimic").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "mimic.info.1"), 370));

        category.entries.values().forEach(e -> e.pageList.stream().filter(p -> p instanceof PageText).forEach(p -> ((PageText) p).setUnicodeFlag(true)));
        book.addCategory(category);
    }

    private static ItemStack getOrbStack(BloodOrb orb) {
        ItemStack ret = new ItemStack(RegistrarBloodMagicItems.BLOOD_ORB);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("orb", BloodMagic.MODID + ":" + orb.getName());
        ret.setTagCompound(tag);
        return ret;
    }
}
