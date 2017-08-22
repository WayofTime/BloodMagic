package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.compat.guideapi.BookUtils;
import WayofTime.bloodmagic.compat.guideapi.entry.EntryText;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class CategoryAlchemy {

    public static void buildCategory(Book book) {
        final String keyBase = "guide." + BloodMagic.MODID + ".entry.alchemy.";

        CategoryItemStack category = new CategoryItemStack(keyBase + "alchemy", new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES));
        category.withKeyBase(BloodMagic.MODID);

        category.addEntry("intro", new EntryText(keyBase + "intro", true));
        category.getEntry("intro").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "intro.info"), 370));

        category.addEntry("ash", new EntryText(keyBase + "ash", true));
        category.getEntry("ash").addPage(BookUtils.getForgeRecipe(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES)));
        category.getEntry("ash").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "ash.info"), 370));

        category.addEntry("speed", new EntryText(keyBase + "speed", true));
        category.getEntry("speed").addPage(BookUtils.getAlchemyPage("movement"));
        category.getEntry("speed").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "speed.info"), 370));

        category.addEntry("updraft", new EntryText(keyBase + "updraft", true));
        category.getEntry("updraft").addPage(BookUtils.getAlchemyPage("updraft"));
        category.getEntry("updraft").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "updraft.info"), 370));

        category.addEntry("turret", new EntryText(keyBase + "turret", true));
        category.getEntry("turret").addPage(BookUtils.getAlchemyPage("skeletonTurret"));
        category.getEntry("turret").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "turret.info"), 370));

        category.addEntry("bounce", new EntryText(keyBase + "bounce", true));
        category.getEntry("bounce").addPage(BookUtils.getAlchemyPage("bounce"));
        category.getEntry("bounce").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "bounce.info"), 370));

        category.addEntry("buff", new EntryText(keyBase + "buff", true));
        category.getEntry("buff").addPage(BookUtils.getAlchemyPage("buff"));
        category.getEntry("buff").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "buff.info"), 370));

        category.addEntry("fastMiner", new EntryText(keyBase + "fastMiner", true));
        category.getEntry("fastMiner").addPage(BookUtils.getAlchemyPage("fastMiner"));
        category.getEntry("fastMiner").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "fastMiner.info"), 370));

        book.addCategory(category);

        for (EntryAbstract entry : category.entries.values()) {
            for (IPage page : entry.pageList) {
                if (page instanceof PageText) {
                    ((PageText) page).setUnicodeFlag(true);
                }
            }
        }
    }
}
