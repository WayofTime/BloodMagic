package WayofTime.bloodmagic.compat.guideapi;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.guideapi.book.*;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.category.CategoryItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;
import java.awt.Color;

@GuideBook
public class GuideBloodMagic implements IGuideBook
{
    public static Book guideBook;

    @Nullable
    @Override
    public Book buildBook() {
        guideBook = new Book();
        guideBook.setTitle("guide.BloodMagic.title");
        guideBook.setDisplayName("guide.BloodMagic.display");
        guideBook.setWelcomeMessage("guide.BloodMagic.welcome");
        guideBook.setAuthor("guide.BloodMagic.author");
        guideBook.setRegistryName(new ResourceLocation(Constants.Mod.MODID, "guide"));
        guideBook.setColor(Color.RED);

        return guideBook;
    }

    @Override
    public void handleModel(ItemStack bookStack) {
        GuideAPI.setModel(guideBook);
    }

    @Override
    public void handlePost(ItemStack bookStack) {
        guideBook.addCategory(new CategoryItemStack(CategoryAlchemy.buildCategory(), "guide.BloodMagic.category.alchemy", new ItemStack(ModItems.ARCANE_ASHES)));
        guideBook.addCategory(new CategoryItemStack(CategoryArchitect.buildCategory(), "guide.BloodMagic.category.architect", new ItemStack(ModItems.SIGIL_DIVINATION)));
        guideBook.addCategory(new CategoryItemStack(CategoryDemon.buildCategory(), "guide.BloodMagic.category.demon", new ItemStack(ModItems.BLOOD_SHARD)));
        guideBook.addCategory(new CategoryItemStack(CategoryRitual.buildCategory(), "guide.BloodMagic.category.ritual", new ItemStack(ModBlocks.RITUAL_CONTROLLER)));
//        guideBook.addCategory(new CategoryItemStack(CategorySpell.buildCategory(), "guide.BloodMagic.category.spell", new ItemStack(ModItems.ritualDiviner)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(GuideAPI.getStackFromBook(GuideBloodMagic.guideBook), new ItemStack(Items.BOOK), Blocks.GLASS, Items.FEATHER));
    }
}
