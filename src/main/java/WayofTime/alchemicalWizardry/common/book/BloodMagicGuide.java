package WayofTime.alchemicalWizardry.common.book;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.guide.PageAltarRecipe;
import WayofTime.alchemicalWizardry.api.guide.PageOrbRecipe;
import WayofTime.alchemicalWizardry.common.guide.RecipeHolder;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryUniText;
import amerifrance.guideapi.pages.PageIRecipe;
import amerifrance.guideapi.pages.PageUnlocImage;

public class BloodMagicGuide 
{
	public static Book bloodMagicGuide;
	public static List<CategoryAbstract> categories = new ArrayList();
	
	public static void registerGuide()
	{
		registerArchitectBook();
		bloodMagicGuide = new Book(categories, "guide.BloodMagic.book.title", "guide.BloodMagic.welcomeMessage", "guide.BloodMagic.book.name", new Color(190, 10, 0));
        GuideRegistry.registerBook(bloodMagicGuide);
	}
	
	public static void registerArchitectBook()
	{
		List<EntryAbstract> entries = new ArrayList();

		ArrayList<IPage> introPages = new ArrayList();
        introPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.intro")));
        entries.add(new EntryUniText(introPages, "guide.BloodMagic.entryName.architect.intro"));
        
		ArrayList<IPage> bloodAltarPages = new ArrayList();
		bloodAltarPages.add(new PageIRecipe(RecipeHolder.bloodAltarRecipe));
		bloodAltarPages.add(new PageIRecipe(RecipeHolder.knifeRecipe));
		bloodAltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodAltar.1")));
		bloodAltarPages.add(new PageAltarRecipe(RecipeHolder.weakBloodOrbRecipe));
		bloodAltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodAltar.2")));
		entries.add(new EntryUniText(bloodAltarPages, "guide.BloodMagic.entryName.architect.bloodAltar"));
		
		ArrayList<IPage> soulNetworkPages = new ArrayList();
		soulNetworkPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.soulNetwork")));
		entries.add(new EntryUniText(soulNetworkPages, "guide.BloodMagic.entryName.architect.soulNetwork"));
		
		ArrayList<IPage> blankSlatePages = new ArrayList();
		blankSlatePages.add(new PageAltarRecipe(RecipeHolder.blankSlateRecipe));
		blankSlatePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.blankSlate")));
		entries.add(new EntryUniText(blankSlatePages, "guide.BloodMagic.entryName.architect.blankSlate"));
		
		ArrayList<IPage> divinationSigilPages = new ArrayList();
		divinationSigilPages.add(new PageOrbRecipe(RecipeHolder.divinationSigilRecipe));
		divinationSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.divination")));
		entries.add(new EntryUniText(divinationSigilPages, "guide.BloodMagic.entryName.architect.divination"));

		ArrayList<IPage> waterSigilPages = new ArrayList();
		waterSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.waterSigil.1")));
		waterSigilPages.add(new PageOrbRecipe(RecipeHolder.waterSigilRecipe));
		waterSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.waterSigil.2")));
		entries.add(new EntryUniText(waterSigilPages, "guide.BloodMagic.entryName.architect.waterSigil"));
		
		ArrayList<IPage> lavaCrystalPages = new ArrayList();
		lavaCrystalPages.add(new PageOrbRecipe(RecipeHolder.lavaCrystalRecipe));
		lavaCrystalPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.lavaCrystal")));
		entries.add(new EntryUniText(lavaCrystalPages, "guide.BloodMagic.entryName.architect.lavaCrystal"));
		
		ArrayList<IPage> hellHarvestPages = new ArrayList();
		hellHarvestPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.hellHarvest")));
		entries.add(new EntryUniText(hellHarvestPages, "guide.BloodMagic.entryName.architect.hellHarvest"));
		
		ArrayList<IPage> lavaSigilPages = new ArrayList();
		lavaSigilPages.add(new PageIRecipe(RecipeHolder.lavaSigilRecipe));
		lavaSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.lavaSigil")));
		entries.add(new EntryUniText(lavaSigilPages, "guide.BloodMagic.entryName.architect.lavaSigil"));

		ArrayList<IPage> blankRunePages = new ArrayList();
		blankRunePages.add(new PageOrbRecipe(RecipeHolder.blankRuneRecipe));
		blankRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.blankRunes.1")));
        blankRunePages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/altars/T2.png"), true));
		blankRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.blankRunes.2")));
		entries.add(new EntryUniText(blankRunePages, "guide.BloodMagic.entryName.architect.blankRunes"));
		
		ArrayList<IPage> speedRunePages = new ArrayList();
		speedRunePages.add(new PageIRecipe(RecipeHolder.speedRuneRecipe));
		speedRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.speedRunes")));
		entries.add(new EntryUniText(speedRunePages, "guide.BloodMagic.entryName.architect.speedRunes"));
		
		ArrayList<IPage> apprenticeOrbPages = new ArrayList();
		apprenticeOrbPages.add(new PageAltarRecipe(RecipeHolder.apprenticeBloodOrbRecipe));
		apprenticeOrbPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.apprenticeOrb")));
		entries.add(new EntryUniText(apprenticeOrbPages, "guide.BloodMagic.entryName.architect.apprenticeOrb"));
		
		ArrayList<IPage> voidSigilPages = new ArrayList();
		voidSigilPages.add(new PageOrbRecipe(RecipeHolder.voidSigilRecipe));
		voidSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.voidSigil")));
		entries.add(new EntryUniText(voidSigilPages, "guide.BloodMagic.entryName.architect.voidSigil"));
		
		ArrayList<IPage> airSigilPages = new ArrayList();
		airSigilPages.add(new PageOrbRecipe(RecipeHolder.airSigilRecipe));
		airSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.airSigil")));
		entries.add(new EntryUniText(airSigilPages, "guide.BloodMagic.entryName.architect.airSigil"));
		
		ArrayList<IPage> sightSigilPages = new ArrayList();
		sightSigilPages.add(new PageOrbRecipe(RecipeHolder.sightSigilRecipe));
		sightSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sightSigil")));
		entries.add(new EntryUniText(sightSigilPages, "guide.BloodMagic.entryName.architect.sightSigil"));
		
		ArrayList<IPage> advancedAltarPages = new ArrayList();
		advancedAltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.advancedAltar")));
		entries.add(new EntryUniText(advancedAltarPages, "guide.BloodMagic.entryName.architect.advancedAltar"));

		ArrayList<IPage> fastMinerPages = new ArrayList();
		fastMinerPages.add(new PageOrbRecipe(RecipeHolder.fastMinerRecipe));
		fastMinerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.fastMiner")));
		entries.add(new EntryUniText(fastMinerPages, "guide.BloodMagic.entryName.architect.fastMiner"));
		
		ArrayList<IPage> soulFrayPages = new ArrayList();
		soulFrayPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.soulFray")));
		entries.add(new EntryUniText(soulFrayPages, "guide.BloodMagic.entryName.architect.soulFray"));
		
		ArrayList<IPage> greenGrovePages = new ArrayList();
		greenGrovePages.add(new PageOrbRecipe(RecipeHolder.greenGroveRecipe));
		greenGrovePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.greenGrove")));
		entries.add(new EntryUniText(greenGrovePages, "guide.BloodMagic.entryName.architect.greenGrove"));
		
		ArrayList<IPage> daggerPages = new ArrayList();
		daggerPages.add(new PageAltarRecipe(RecipeHolder.daggerRecipe));
		daggerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.dagger")));
		entries.add(new EntryUniText(daggerPages, "guide.BloodMagic.entryName.architect.dagger"));
		
		ArrayList<IPage> sacrificePages = new ArrayList();
		sacrificePages.add(new PageIRecipe(RecipeHolder.selfSacrificeRuneRecipe));
		sacrificePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sacrifice.1")));
		sacrificePages.add(new PageIRecipe(RecipeHolder.sacrificeRuneRecipe));
		sacrificePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sacrifice.2")));
		entries.add(new EntryUniText(sacrificePages, "guide.BloodMagic.entryName.architect.sacrifice"));
		
		ArrayList<IPage> bloodPackPages = new ArrayList();
		bloodPackPages.add(new PageIRecipe(RecipeHolder.bloodPackRecipe));
		bloodPackPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodPack")));
		entries.add(new EntryUniText(bloodPackPages, "guide.BloodMagic.entryName.architect.bloodPack"));
		
		ArrayList<IPage> fivePeoplePages = new ArrayList();
		fivePeoplePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.fivePeople")));
		entries.add(new EntryUniText(fivePeoplePages, "guide.BloodMagic.entryName.architect.fivePeople"));
		
		
		
		
		
        categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.architect", new ItemStack(ModItems.divinationSigil)));
	}
}
