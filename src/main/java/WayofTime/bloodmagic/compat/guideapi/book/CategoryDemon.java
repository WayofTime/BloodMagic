package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class CategoryDemon {

    public static void buildCategory(Book book) {
        final String keyBase = "guide." + BloodMagic.MODID + ".entry.demon.";

        CategoryItemStack category = new CategoryItemStack(keyBase + "demon", new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD));
        category.withKeyBase(BloodMagic.MODID);

        category.addEntry("intro", new EntryText(keyBase + "intro", true));
        category.getEntry("intro").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "intro.info"), 370));

        category.addEntry("snare", new EntryText(keyBase + "snare", true));
        category.getEntry("snare").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "snare.info.1"), 370));
        category.getEntry("snare").addPage(BookUtils.getCraftingPage("soul_snare"));
        category.getEntry("snare").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "snare.info.2"), 370));

        category.addEntry("forge", new EntryText(keyBase + "forge", true));
        category.getEntry("forge").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "forge.info.1"), 370));
        category.getEntry("forge").addPage(BookUtils.getCraftingPage("soul_forge"));
        category.getEntry("forge").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "forge.info.2"), 370));

        category.addEntry("petty", new EntryText(keyBase + "petty", true));
        category.getEntry("petty").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "petty.info.1"), 370));
        category.getEntry("petty").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM)));
        category.getEntry("petty").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "petty.info.2"), 370));

        category.addEntry("sword", new EntryText(keyBase + "sword", true));
        category.getEntry("sword").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "sword.info.1"), 370));
        category.getEntry("sword").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SWORD)));
        category.getEntry("sword").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "sword.info.2"), 370));

        category.addEntry("lesser", new EntryText(keyBase + "lesser", true));
        category.getEntry("lesser").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lesser.info.1"), 370));
        category.getEntry("lesser").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1)));
        category.getEntry("lesser").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lesser.info.2"), 370));

        category.addEntry("reactions", new EntryText(keyBase + "reactions", true));
        category.getEntry("reactions").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "reactions.info"), 370));

        category.addEntry("sentientGem", new EntryText(keyBase + "sentientGem", true));
        category.getEntry("sentientGem").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "sentientGem.info.1"), 370));
        category.getEntry("sentientGem").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "sentientGem.info.2"), 370));

        category.addEntry("routing", new EntryText(keyBase + "routing", true));
        category.getEntry("routing").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE)));
        category.getEntry("routing").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE)));
        category.getEntry("routing").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE)));
        category.getEntry("routing").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE)));
        category.getEntry("routing").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.NODE_ROUTER)));
        category.getEntry("routing").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "routing.info"), 370));

        category.addEntry("aura", new EntryText(keyBase + "aura", true));
        category.getEntry("aura").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "aura.info"), 370));

        category.addEntry("types", new EntryText(keyBase + "types", true));
        category.getEntry("types").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "types.info"), 370));

        category.addEntry("crucible", new EntryText(keyBase + "crucible", true));
        category.getEntry("crucible").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRUCIBLE)));
        category.getEntry("crucible").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "crucible.info"), 370));

        category.addEntry("crystallizer", new EntryText(keyBase + "crystallizer", true));
        category.getEntry("crystallizer").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTALLIZER)));
        category.getEntry("crystallizer").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "crystallizer.info"), 370));

        category.addEntry("cluster", new EntryText(keyBase + "cluster", true));
        category.getEntry("cluster").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL)));
        category.getEntry("cluster").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "cluster.info"), 370));

        category.addEntry("pylon", new EntryText(keyBase + "pylon", true));
        category.getEntry("pylon").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicBlocks.DEMON_PYLON)));
        category.getEntry("pylon").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "pylon.info"), 370));

        category.addEntry("gauge", new EntryText(keyBase + "gauge", true));
        category.getEntry("gauge").addPage(BookUtils.getForgePage(new ItemStack(RegistrarBloodMagicItems.DEMON_WILL_GAUGE)));
        category.getEntry("gauge").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "gauge.info"), 370));

        category.entries.values().forEach(e -> e.pageList.stream().filter(p -> p instanceof PageText).forEach(p -> ((PageText) p).setUnicodeFlag(true)));
        book.addCategory(category);
    }
}
