//package WayofTime.bloodmagic.compat.guideapi;
//
//import WayofTime.bloodmagic.compat.guideapi.book.*;
//import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
//import WayofTime.bloodmagic.registry.ModBlocks;
//import WayofTime.bloodmagic.registry.ModItems;
//import amerifrance.guideapi.api.GuideAPI;
//import amerifrance.guideapi.api.impl.Book;
//import amerifrance.guideapi.api.util.NBTBookTags;
//import amerifrance.guideapi.category.CategoryItemStack;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.relauncher.Side;
//
//import java.awt.Color;
//
//public class GuideBloodMagic {
//
//    public static Book guideBook;
//
//    public static void initBook() {
//        guideBook = new Book();
//        guideBook.setTitle("guide.BloodMagic.title");
//        guideBook.setDisplayName("guide.BloodMagic.display");
//        guideBook.setWelcomeMessage("guide.BloodMagic.welcome");
//        guideBook.setAuthor("guide.BloodMagic.author");
//        guideBook.setRegistryName("BloodMagic");
//        guideBook.setColor(Color.RED);
//
//        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//            GuideAPI.setModel(guideBook);
//    }
//
//    public static void initCategories() {
//        guideBook.addCategory(new CategoryItemStack(CategoryAlchemy.buildCategory(), "guide.BloodMagic.category.alchemy", new ItemStack(ModItems.arcaneAshes)));
//        guideBook.addCategory(new CategoryItemStack(CategoryArchitect.buildCategory(), "guide.BloodMagic.category.architect", new ItemStack(ModItems.sigilDivination)));
//        guideBook.addCategory(new CategoryItemStack(CategoryDemon.buildCategory(), "guide.BloodMagic.category.demon", new ItemStack(ModItems.bloodShard)));
//        guideBook.addCategory(new CategoryItemStack(CategoryRitual.buildCategory(), "guide.BloodMagic.category.ritual", new ItemStack(ModBlocks.ritualController)));
//        guideBook.addCategory(new CategoryItemStack(CategorySpell.buildCategory(), "guide.BloodMagic.category.spell", new ItemStack(ModItems.ritualDiviner)));
//    }
//
//    public static void initJEIBlacklist() {
//        if (Loader.isModLoaded("JEI"))
//            BloodMagicPlugin.jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(
//                    GuideAPI.guideBook,
//                    NBTBookTags.BOOK_TAG,
//                    NBTBookTags.CATEGORY_PAGE_TAG,
//                    NBTBookTags.CATEGORY_TAG,
//                    NBTBookTags.ENTRY_PAGE_TAG,
//                    NBTBookTags.ENTRY_TAG,
//                    NBTBookTags.KEY_TAG,
//                    NBTBookTags.PAGE_TAG
//                    );
//    }
//}
