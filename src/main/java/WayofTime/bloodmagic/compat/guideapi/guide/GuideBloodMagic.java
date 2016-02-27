package WayofTime.bloodmagic.compat.guideapi.guide;

import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.guideapi.guide.page.PageAltarRecipe;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.GuideAPIItems;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import lombok.Getter;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GuideBloodMagic
{

    @Getter
    private static Book bloodMagicGuide;

    public static List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();

    public static void initGuide()
    {
        bloodMagicGuide = new Book();
        bloodMagicGuide.setAuthor("guide.BloodMagic.book.author");
        bloodMagicGuide.setUnlocBookTitle("guide.BloodMagic.book.title");
        bloodMagicGuide.setUnlocDisplayName("guide.BloodMagic.book.display");
        bloodMagicGuide.setUnlocWelcomeMessage("guide.BloodMagic.book.welcome");
        bloodMagicGuide.setBookColor(new Color(0xFF100F));

        addArchitect();

        bloodMagicGuide.setCategoryList(categories);

        GuideRegistry.registerBook(bloodMagicGuide, null);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ModelLoader.setCustomModelResourceLocation(GuideAPIItems.guideBook, GuideRegistry.getIndexOf(bloodMagicGuide), new ModelResourceLocation(new ResourceLocation("guideapi", "ItemGuideBook"), "type=book"));
    }

    private static void addArchitect()
    {
        List<EntryAbstract> entries = new ArrayList<EntryAbstract>();

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText(TextHelper.localize("guide.BloodMagic.entry.architect.intro.1"), 315));
        entries.add(new EntryItemStack(introPages, "guide.BloodMagic.entry.architect.intro", new ItemStack(Items.writable_book)));

        List<IPage> bloodAltarPages = new ArrayList<IPage>();
        bloodAltarPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.altar), "a a", "aba", "cdc", 'a', "stone", 'b', Blocks.furnace, 'c', "ingotGold", 'd', new ItemStack(ModItems.monsterSoul))));
        bloodAltarPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sacrificialDagger), "aaa", " ba", "c a", 'a', "blockGlass", 'b', "ingotGold", 'c', "ingotIron")));
        bloodAltarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize("guide.BloodMagic.entry.architect.bloodAltar.1"), 340));
        bloodAltarPages.add(new PageAltarRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1)));
        bloodAltarPages.addAll(PageHelper.pagesForLongText(TextHelper.localize("guide.BloodMagic.entry.architect.bloodAltar.2")));
        entries.add(new EntryItemStack(bloodAltarPages, "guide.BloodMagic.entry.architect.bloodAltar", new ItemStack(ModBlocks.altar)));

        categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.architect.name", new ItemStack(ModBlocks.altar)));
    }
}
