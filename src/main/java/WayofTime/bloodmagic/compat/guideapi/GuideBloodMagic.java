package WayofTime.bloodmagic.compat.guideapi;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.compat.guideapi.book.CategoryAlchemy;
import WayofTime.bloodmagic.compat.guideapi.book.CategoryArchitect;
import WayofTime.bloodmagic.compat.guideapi.book.CategoryDemon;
import WayofTime.bloodmagic.compat.guideapi.book.CategoryRitual;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.category.CategoryItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;

@GuideBook(priority = EventPriority.HIGHEST)
public class GuideBloodMagic implements IGuideBook {

    public static final Book GUIDE_BOOK = new Book();

    @Nullable
    @Override
    public Book buildBook() {
        GUIDE_BOOK.setTitle("guide.bloodmagic.title");
        GUIDE_BOOK.setDisplayName("guide.bloodmagic.display");
        GUIDE_BOOK.setWelcomeMessage("guide.bloodmagic.welcome");
        GUIDE_BOOK.setAuthor("guide.bloodmagic.author");
        GUIDE_BOOK.setRegistryName(new ResourceLocation(BloodMagic.MODID, "guide"));
        GUIDE_BOOK.setColor(Color.RED);

        return GUIDE_BOOK;
    }

    @Override
    public void handlePost(ItemStack bookStack) {
        GUIDE_BOOK.addCategory(new CategoryItemStack(CategoryAlchemy.buildCategory(), "guide.bloodmagic.category.alchemy", new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES)));
        GUIDE_BOOK.addCategory(new CategoryItemStack(CategoryArchitect.buildCategory(), "guide.bloodmagic.category.architect", new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION)));
        GUIDE_BOOK.addCategory(new CategoryItemStack(CategoryDemon.buildCategory(), "guide.bloodmagic.category.demon", new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)));
        GUIDE_BOOK.addCategory(new CategoryItemStack(CategoryRitual.buildCategory(), "guide.bloodmagic.category.ritual", new ItemStack(RegistrarBloodMagicBlocks.RITUAL_CONTROLLER)));
//        guideBook.addCategory(new CategoryItemStack(CategorySpell.buildCategory(), "guide.bloodmagic.category.spell", new ItemStack(ModItems.ritualDiviner)));
    }

    @Nullable
    @Override
    public IRecipe getRecipe(@Nonnull ItemStack bookStack) {
        return new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "guide"), GuideAPI.getStackFromBook(GUIDE_BOOK), new ItemStack(Items.BOOK), "blockGlass", "feather").setRegistryName("bloodmagic_guide");
    }
}
