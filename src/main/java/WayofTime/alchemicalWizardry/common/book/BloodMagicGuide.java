package WayofTime.alchemicalWizardry.common.book;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
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
		
		ArrayList<IPage> tier3Pages = new ArrayList();
        tier3Pages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/altars/T3.png"), true));
        tier3Pages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.tier3")));
		entries.add(new EntryUniText(tier3Pages, "guide.BloodMagic.entryName.architect.tier3"));
		
		ArrayList<IPage> magicianOrbPages = new ArrayList();
		magicianOrbPages.add(new PageAltarRecipe(RecipeHolder.magicianBloodOrbRecipe));
		magicianOrbPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.magicianOrb")));
		entries.add(new EntryUniText(magicianOrbPages, "guide.BloodMagic.entryName.architect.magicianOrb"));
		
		ArrayList<IPage> newRunePages = new ArrayList();
		newRunePages.add(new PageOrbRecipe(RecipeHolder.capacityRuneRecipe));
		newRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.newRune.1")));
		newRunePages.add(new PageOrbRecipe(RecipeHolder.dislocationRuneRecipe));
		newRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.newRune.2")));
		entries.add(new EntryUniText(newRunePages, "guide.BloodMagic.entryName.architect.newRune"));
		
		ArrayList<IPage> magnetismPages = new ArrayList();
		magnetismPages.add(new PageOrbRecipe(RecipeHolder.magnetismSigilRecipe));
		magnetismPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.magnetism")));
		entries.add(new EntryUniText(magnetismPages, "guide.BloodMagic.entryName.architect.magnetism"));
		
		ArrayList<IPage> phantomBridgePages = new ArrayList();
		phantomBridgePages.add(new PageOrbRecipe(RecipeHolder.phantomBridgeRecipe));
		phantomBridgePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.phantomBridge")));
		entries.add(new EntryUniText(phantomBridgePages, "guide.BloodMagic.entryName.architect.phantomBridge"));
		
		ArrayList<IPage> holdingPages = new ArrayList();
		holdingPages.add(new PageOrbRecipe(RecipeHolder.holdingSigilRecipe));
		holdingPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.holding")));
		entries.add(new EntryUniText(holdingPages, "guide.BloodMagic.entryName.architect.holding"));
		
		ArrayList<IPage> elementalAffinityPages = new ArrayList();
		elementalAffinityPages.add(new PageOrbRecipe(RecipeHolder.affinitySigilRecipe));
		elementalAffinityPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.elementalAffinity")));
		entries.add(new EntryUniText(elementalAffinityPages, "guide.BloodMagic.entryName.architect.elementalAffinity"));
		
		ArrayList<IPage> ritualStonesPages = new ArrayList();
		ritualStonesPages.add(new PageOrbRecipe(RecipeHolder.ritualStoneRecipe));
		ritualStonesPages.add(new PageOrbRecipe(RecipeHolder.masterStoneRecipe));
		ritualStonesPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.ritualStones")));
		entries.add(new EntryUniText(ritualStonesPages, "guide.BloodMagic.entryName.architect.ritualStones"));

		ArrayList<IPage> bloodLampPages = new ArrayList();
		bloodLampPages.add(new PageOrbRecipe(RecipeHolder.bloodLampRecipe));
		bloodLampPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodLamp")));
		entries.add(new EntryUniText(bloodLampPages, "guide.BloodMagic.entryName.architect.bloodLamp"));
		
		ArrayList<IPage> boundArmourPages = new ArrayList();
		boundArmourPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.boundArmour.1")));
		boundArmourPages.add(new PageIRecipe(RecipeHolder.emptySocketRecipe));
		boundArmourPages.add(new PageAltarRecipe(RecipeHolder.filledSocketRecipe));
		boundArmourPages.add(new PageOrbRecipe(RecipeHolder.soulForgeRecipe));
		boundArmourPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.boundArmour.2")));
		entries.add(new EntryUniText(boundArmourPages, "guide.BloodMagic.entryName.architect.boundArmour"));
		
		if(AlchemicalWizardry.isThaumcraftLoaded)
		{
			ArrayList<IPage> sanguineArmourPages = new ArrayList();
			sanguineArmourPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sanguineArmour"), new ItemStack(ModItems.sanguineRobe)));
			entries.add(new EntryUniText(sanguineArmourPages, "guide.BloodMagic.entryName.architect.sanguineArmour"));
		}
		
		ArrayList<IPage> soulSuppressPages = new ArrayList();
		soulSuppressPages.add(new PageIRecipe(RecipeHolder.inhibitorRecipe));
		soulSuppressPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.soulSuppress")));
		entries.add(new EntryUniText(soulSuppressPages, "guide.BloodMagic.entryName.architect.soulSuppress"));
		
		ArrayList<IPage> ritualDivinerPages = new ArrayList();
		ritualDivinerPages.add(new PageIRecipe(RecipeHolder.ritualDiviner1Recipe));
		ritualDivinerPages.add(new PageIRecipe(RecipeHolder.ritualDiviner2Recipe));
		ritualDivinerPages.add(new PageIRecipe(RecipeHolder.ritualDiviner3Recipe));
		ritualDivinerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.ritualDiviner")));
		entries.add(new EntryUniText(ritualDivinerPages, "guide.BloodMagic.entryName.architect.ritualDiviner"));
		
		ArrayList<IPage> bloodShardPages = new ArrayList();
		bloodShardPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodShard"), new ItemStack(ModItems.weakBloodShard)));
		entries.add(new EntryUniText(bloodShardPages, "guide.BloodMagic.entryName.architect.bloodShard"));
		
		ArrayList<IPage> tier4AltarPages = new ArrayList();
		tier4AltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.tier4Altar.1")));
        tier4AltarPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/altars/T4.png"), true));
		tier4AltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.tier4Altar.2")));
		entries.add(new EntryUniText(tier4AltarPages, "guide.BloodMagic.entryName.architect.tier4Altar"));
		
		ArrayList<IPage> masterOrbPages = new ArrayList();
		masterOrbPages.add(new PageAltarRecipe(RecipeHolder.masterBloodOrbRecipe));
		masterOrbPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.masterOrb")));
		entries.add(new EntryUniText(masterOrbPages, "guide.BloodMagic.entryName.architect.masterOrb"));
		
		ArrayList<IPage> whirlwindPages = new ArrayList();
		whirlwindPages.add(new PageOrbRecipe(RecipeHolder.whirlwindSigilRecipe));
		whirlwindPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.whirlwind")));
		entries.add(new EntryUniText(whirlwindPages, "guide.BloodMagic.entryName.architect.whirlwind"));
		
		ArrayList<IPage> compressionPages = new ArrayList();
		compressionPages.add(new PageOrbRecipe(RecipeHolder.compressionSigilRecipe));
		compressionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.compression")));
		entries.add(new EntryUniText(compressionPages, "guide.BloodMagic.entryName.architect.compression"));
		
		ArrayList<IPage> severancePages = new ArrayList();
		severancePages.add(new PageOrbRecipe(RecipeHolder.enderSeveranceSigilRecipe));
		severancePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.severance")));
		entries.add(new EntryUniText(severancePages, "guide.BloodMagic.entryName.architect.severance"));
		
		ArrayList<IPage> teleposerPages = new ArrayList();
		teleposerPages.add(new PageAltarRecipe(RecipeHolder.teleposerFocusRecipe1));
		teleposerPages.add(new PageIRecipe(RecipeHolder.teleposerRecipe));
		teleposerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.teleposer")));
		entries.add(new EntryUniText(teleposerPages, "guide.BloodMagic.entryName.architect.teleposer"));
		
		ArrayList<IPage> suppressionPages = new ArrayList();
		suppressionPages.add(new PageOrbRecipe(RecipeHolder.suppressionSigilRecipe));
		suppressionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.suppression")));
		entries.add(new EntryUniText(suppressionPages, "guide.BloodMagic.entryName.architect.suppression"));
		
		ArrayList<IPage> superiorCapacityPages = new ArrayList();
		superiorCapacityPages.add(new PageOrbRecipe(RecipeHolder.superiorCapacityRecipe));
		superiorCapacityPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.superiorCapacity")));
		entries.add(new EntryUniText(superiorCapacityPages, "guide.BloodMagic.entryName.architect.superiorCapacity"));
		
		ArrayList<IPage> orbRunePages = new ArrayList();
		orbRunePages.add(new PageOrbRecipe(RecipeHolder.orbRuneRecipe));
		orbRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.orbRune")));
		entries.add(new EntryUniText(orbRunePages, "guide.BloodMagic.entryName.architect.orbRune"));
		
		ArrayList<IPage> fieldTripPages = new ArrayList();
		fieldTripPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.fieldTrip")));
		entries.add(new EntryUniText(fieldTripPages, "guide.BloodMagic.entryName.architect.fieldTrip"));
		
        categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.architect", new ItemStack(ModItems.divinationSigil)));
	}
}
