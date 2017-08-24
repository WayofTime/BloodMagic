package WayofTime.bloodmagic.compat.guideapi.book;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
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

public class CategoryRitual {
    static final String keyBase = "guide." + BloodMagic.MODID + ".entry.ritual.";

    public static void buildCategory(Book book) {
        CategoryItemStack category = new CategoryItemStack(keyBase + "ritual", new ItemStack(RegistrarBloodMagicBlocks.RITUAL_CONTROLLER));
        category.withKeyBase(BloodMagic.MODID);

        category.addEntry("intro", new EntryText(keyBase + "intro", true));
        category.getEntry("intro").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "intro.info")));

        category.addEntry("basics", new EntryText(keyBase + "basics", true));
        category.getEntry("basics").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "basics.info")));

        category.addEntry("ritualStone", new EntryText(keyBase + "ritualStone", true));
        category.getEntry("ritualStone").addPage(BookUtils.getCraftingPage("ritual_stone_blank"));
        category.getEntry("ritualStone").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "ritualStone.info.1"), 370));
        for (EnumRuneType type : EnumRuneType.values())
            category.getEntry("ritualStone").addPage(BookUtils.getAltarPage(type.getScribeStack()));
        category.getEntry("ritualStone").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "ritualStone.info.2"), 370));

        category.addEntry("masterRitualStone", new EntryText(keyBase + "masterRitualStone", true));
        category.getEntry("masterRitualStone").addPage(BookUtils.getCraftingPage("ritual_controller_master"));
        category.getEntry("masterRitualStone").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "masterRitualStone.info"), 370));

        category.addEntry("activationCrystal", new EntryText(keyBase + "activationCrystal", true));
        category.getEntry("activationCrystal").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "activationCrystal.info.1"), 370));
        category.getEntry("activationCrystal").addPage(BookUtils.getAltarPage(new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL)));
        category.getEntry("activationCrystal").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "activationCrystal.info.2"), 370));

        category.addEntry("diviner", new EntryText(keyBase + "diviner", true));
        category.getEntry("diviner").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "diviner.info.1"), 370));
        category.getEntry("diviner").addPage(BookUtils.getCraftingPage("ritual_diviner_0"));
        category.getEntry("diviner").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "diviner.info.2"), 370));

        category.addEntry("fullSpring", new EntryText(keyBase + "fullSpring", true));
        category.getEntry("fullSpring").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "fullSpring.info")));

        category.addEntry("lava", new EntryText(keyBase + "lava", true));
        category.getEntry("lava").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "lava.info")));

        category.addEntry("greenGrove", new EntryText(keyBase + "greenGrove", true));
        category.getEntry("greenGrove").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "greenGrove.info")));

        category.addEntry("magnetism", new EntryText(keyBase + "magnetism", true));
        category.getEntry("magnetism").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "magnetism.info")));

        category.addEntry("crusher", new EntryText(keyBase + "crusher", true));
        category.getEntry("crusher").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "crusher.info")));

        category.addEntry("highJump", new EntryText(keyBase + "highJump", true));
        category.getEntry("highJump").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "highJump.info")));

        category.addEntry("speed", new EntryText(keyBase + "speed", true));
        category.getEntry("speed").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "speed.info")));

        category.addEntry("wellOfSuffering", new EntryText(keyBase + "wellOfSuffering", true));
        category.getEntry("wellOfSuffering").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "wellOfSuffering.info")));

        category.addEntry("featheredKnife", new EntryText(keyBase + "featheredKnife", true));
        category.getEntry("featheredKnife").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "featheredKnife.info")));

        category.addEntry("regen", new EntryText(keyBase + "regen", true));
        category.getEntry("regen").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "regen.info")));

        category.addEntry("harvest", new EntryText(keyBase + "harvest", true));
        category.getEntry("harvest").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "harvest.info")));

        category.addEntry("interdiction", new EntryText(keyBase + "interdiction", true));
        category.getEntry("interdiction").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "interdiction.info")));

        category.addEntry("containment", new EntryText(keyBase + "containment", true));
        category.getEntry("containment").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "containment.info")));

        category.addEntry("suppression", new EntryText(keyBase + "suppression", true));
        category.getEntry("suppression").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "suppression.info")));

        category.addEntry("expulsion", new EntryText(keyBase + "expulsion", true));
        category.getEntry("expulsion").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "expulsion.info")));

        category.addEntry("zephyr", new EntryText(keyBase + "zephyr", true));
        category.getEntry("zephyr").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "zephyr.info")));

        category.addEntry("laying", new EntryText(keyBase + "laying", true));
        category.getEntry("laying").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "laying.info")));

        category.addEntry("timberman", new EntryText(keyBase + "timberman", true));
        category.getEntry("timberman").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "timberman.info")));

        category.addEntry("meteor", new EntryText(keyBase + "meteor", true));
        category.getEntry("meteor").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "meteor.info")));

        category.addEntry("downgrade", new EntryText(keyBase + "downgrade", true));
        category.getEntry("downgrade").addPageList(PageHelper.pagesForLongText(I18n.format(keyBase + "downgrade.info")));

        category.entries.values().forEach(e -> e.pageList.stream().filter(p -> p instanceof PageText).forEach(p -> ((PageText) p).setUnicodeFlag(true)));
        book.addCategory(category);
    }
}
