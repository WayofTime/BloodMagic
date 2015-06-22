package WayofTime.alchemicalWizardry.common.book;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.guide.OrbRecipeRenderer;
import WayofTime.alchemicalWizardry.api.guide.PageAltarRecipe;
import WayofTime.alchemicalWizardry.common.guide.RecipeHolder;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.util.BookBuilder;
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
		registerRitualBook();
		registerDemonBook();
		registerSpellBook();
		registerAlchemyBook();
		
		BookBuilder bmBookBuilder = new BookBuilder();
		bmBookBuilder.setCategories(categories).setUnlocBookTitle("guide.BloodMagic.book.title").setUnlocWelcomeMessage("guide.BloodMagic.welcomeMessage").setUnlocDisplayName("guide.BloodMagic.book.name").setBookColor(new Color(190, 10, 0)).setAuthor("--Blood Magic");
		
//		bloodMagicGuide = new Book(categories, "guide.BloodMagic.book.title", "guide.BloodMagic.welcomeMessage", "guide.BloodMagic.book.name", new Color(190, 10, 0));
		bloodMagicGuide = bmBookBuilder.build();
        GuideRegistry.registerBook(bloodMagicGuide);
	}
	
	public static PageIRecipe getOrbPageForRecipe(IRecipe recipe)
	{
		return new PageIRecipe(recipe, new OrbRecipeRenderer(recipe));
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
		divinationSigilPages.add(getOrbPageForRecipe(RecipeHolder.divinationSigilRecipe));
		divinationSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.divination")));
		entries.add(new EntryUniText(divinationSigilPages, "guide.BloodMagic.entryName.architect.divination"));

		ArrayList<IPage> waterSigilPages = new ArrayList();
		waterSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.waterSigil.1")));
		waterSigilPages.add(getOrbPageForRecipe(RecipeHolder.waterSigilRecipe));
		waterSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.waterSigil.2")));
		entries.add(new EntryUniText(waterSigilPages, "guide.BloodMagic.entryName.architect.waterSigil"));
		
		ArrayList<IPage> lavaCrystalPages = new ArrayList();
		lavaCrystalPages.add(getOrbPageForRecipe(RecipeHolder.lavaCrystalRecipe));
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
		blankRunePages.add(getOrbPageForRecipe(RecipeHolder.blankRuneRecipe));
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
		voidSigilPages.add(getOrbPageForRecipe(RecipeHolder.voidSigilRecipe));
		voidSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.voidSigil")));
		entries.add(new EntryUniText(voidSigilPages, "guide.BloodMagic.entryName.architect.voidSigil"));
		
		ArrayList<IPage> airSigilPages = new ArrayList();
		airSigilPages.add(getOrbPageForRecipe(RecipeHolder.airSigilRecipe));
		airSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.airSigil")));
		entries.add(new EntryUniText(airSigilPages, "guide.BloodMagic.entryName.architect.airSigil"));
		
		ArrayList<IPage> sightSigilPages = new ArrayList();
		sightSigilPages.add(getOrbPageForRecipe(RecipeHolder.sightSigilRecipe));
		sightSigilPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sightSigil")));
		entries.add(new EntryUniText(sightSigilPages, "guide.BloodMagic.entryName.architect.sightSigil"));
		
		ArrayList<IPage> advancedAltarPages = new ArrayList();
		advancedAltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.advancedAltar")));
		entries.add(new EntryUniText(advancedAltarPages, "guide.BloodMagic.entryName.architect.advancedAltar"));

		ArrayList<IPage> fastMinerPages = new ArrayList();
		fastMinerPages.add(getOrbPageForRecipe(RecipeHolder.fastMinerRecipe));
		fastMinerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.fastMiner")));
		entries.add(new EntryUniText(fastMinerPages, "guide.BloodMagic.entryName.architect.fastMiner"));
		
		ArrayList<IPage> soulFrayPages = new ArrayList();
		soulFrayPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.soulFray")));
		entries.add(new EntryUniText(soulFrayPages, "guide.BloodMagic.entryName.architect.soulFray"));
		
		ArrayList<IPage> greenGrovePages = new ArrayList();
		greenGrovePages.add(getOrbPageForRecipe(RecipeHolder.greenGroveRecipe));
		greenGrovePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.greenGrove")));
		entries.add(new EntryUniText(greenGrovePages, "guide.BloodMagic.entryName.architect.greenGrove"));
		
		ArrayList<IPage> daggerPages = new ArrayList();
		daggerPages.add(new PageAltarRecipe(RecipeHolder.daggerRecipe));
		daggerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.dagger")));
		entries.add(new EntryUniText(daggerPages, "guide.BloodMagic.entryName.architect.dagger"));
		
		ArrayList<IPage> sacrificePages = new ArrayList();
		sacrificePages.add(getOrbPageForRecipe(RecipeHolder.selfSacrificeRuneRecipe));
		sacrificePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.sacrifice.1")));
		sacrificePages.add(getOrbPageForRecipe(RecipeHolder.sacrificeRuneRecipe));
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
		newRunePages.add(getOrbPageForRecipe(RecipeHolder.capacityRuneRecipe));
		newRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.newRune.1")));
		newRunePages.add(getOrbPageForRecipe(RecipeHolder.dislocationRuneRecipe));
		newRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.newRune.2")));
		entries.add(new EntryUniText(newRunePages, "guide.BloodMagic.entryName.architect.newRune"));
		
		ArrayList<IPage> magnetismPages = new ArrayList();
		magnetismPages.add(getOrbPageForRecipe(RecipeHolder.magnetismSigilRecipe));
		magnetismPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.magnetism")));
		entries.add(new EntryUniText(magnetismPages, "guide.BloodMagic.entryName.architect.magnetism"));
		
		ArrayList<IPage> phantomBridgePages = new ArrayList();
		phantomBridgePages.add(getOrbPageForRecipe(RecipeHolder.phantomBridgeRecipe));
		phantomBridgePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.phantomBridge")));
		entries.add(new EntryUniText(phantomBridgePages, "guide.BloodMagic.entryName.architect.phantomBridge"));
		
		ArrayList<IPage> holdingPages = new ArrayList();
		holdingPages.add(getOrbPageForRecipe(RecipeHolder.holdingSigilRecipe));
		holdingPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.holding")));
		entries.add(new EntryUniText(holdingPages, "guide.BloodMagic.entryName.architect.holding"));
		
		ArrayList<IPage> elementalAffinityPages = new ArrayList();
		elementalAffinityPages.add(getOrbPageForRecipe(RecipeHolder.affinitySigilRecipe));
		elementalAffinityPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.elementalAffinity")));
		entries.add(new EntryUniText(elementalAffinityPages, "guide.BloodMagic.entryName.architect.elementalAffinity"));
		
		ArrayList<IPage> ritualStonesPages = new ArrayList();
		ritualStonesPages.add(getOrbPageForRecipe(RecipeHolder.ritualStoneRecipe));
		ritualStonesPages.add(getOrbPageForRecipe(RecipeHolder.masterStoneRecipe));
		ritualStonesPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.ritualStones")));
		entries.add(new EntryUniText(ritualStonesPages, "guide.BloodMagic.entryName.architect.ritualStones"));

		ArrayList<IPage> bloodLampPages = new ArrayList();
		bloodLampPages.add(getOrbPageForRecipe(RecipeHolder.bloodLampRecipe));
		bloodLampPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bloodLamp")));
		entries.add(new EntryUniText(bloodLampPages, "guide.BloodMagic.entryName.architect.bloodLamp"));
		
		ArrayList<IPage> boundArmourPages = new ArrayList();
		boundArmourPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.boundArmour.1")));
		boundArmourPages.add(new PageIRecipe(RecipeHolder.emptySocketRecipe));
		boundArmourPages.add(new PageAltarRecipe(RecipeHolder.filledSocketRecipe));
		boundArmourPages.add(getOrbPageForRecipe(RecipeHolder.soulForgeRecipe));
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
		whirlwindPages.add(getOrbPageForRecipe(RecipeHolder.whirlwindSigilRecipe));
		whirlwindPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.whirlwind")));
		entries.add(new EntryUniText(whirlwindPages, "guide.BloodMagic.entryName.architect.whirlwind"));
		
		ArrayList<IPage> compressionPages = new ArrayList();
		compressionPages.add(getOrbPageForRecipe(RecipeHolder.compressionSigilRecipe));
		compressionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.compression")));
		entries.add(new EntryUniText(compressionPages, "guide.BloodMagic.entryName.architect.compression"));
		
		ArrayList<IPage> severancePages = new ArrayList();
		severancePages.add(getOrbPageForRecipe(RecipeHolder.enderSeveranceSigilRecipe));
		severancePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.severance")));
		entries.add(new EntryUniText(severancePages, "guide.BloodMagic.entryName.architect.severance"));
		
		ArrayList<IPage> teleposerPages = new ArrayList();
		teleposerPages.add(new PageAltarRecipe(RecipeHolder.teleposerFocusRecipe1));
		teleposerPages.add(new PageIRecipe(RecipeHolder.teleposerRecipe));
		teleposerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.teleposer")));
		entries.add(new EntryUniText(teleposerPages, "guide.BloodMagic.entryName.architect.teleposer"));
		
		ArrayList<IPage> suppressionPages = new ArrayList();
		suppressionPages.add(getOrbPageForRecipe(RecipeHolder.suppressionSigilRecipe));
		suppressionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.suppression")));
		entries.add(new EntryUniText(suppressionPages, "guide.BloodMagic.entryName.architect.suppression"));
		
		ArrayList<IPage> superiorCapacityPages = new ArrayList();
		superiorCapacityPages.add(getOrbPageForRecipe(RecipeHolder.superiorCapacityRecipe));
		superiorCapacityPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.superiorCapacity")));
		entries.add(new EntryUniText(superiorCapacityPages, "guide.BloodMagic.entryName.architect.superiorCapacity"));
		
		ArrayList<IPage> orbRunePages = new ArrayList();
		orbRunePages.add(getOrbPageForRecipe(RecipeHolder.orbRuneRecipe));
		orbRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.orbRune")));
		entries.add(new EntryUniText(orbRunePages, "guide.BloodMagic.entryName.architect.orbRune"));
		
		ArrayList<IPage> fieldTripPages = new ArrayList();
		fieldTripPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.fieldTrip")));
		entries.add(new EntryUniText(fieldTripPages, "guide.BloodMagic.entryName.architect.fieldTrip"));
		
		ArrayList<IPage> bindingKeyPages = new ArrayList();
		bindingKeyPages.add(new PageIRecipe(RecipeHolder.keyOfBindingRecipe));
		bindingKeyPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.bindingKey")));
		entries.add(new EntryUniText(bindingKeyPages, "guide.BloodMagic.entryName.architect.bindingKey"));
		
		ArrayList<IPage> tier5AltarPages = new ArrayList();
        tier5AltarPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/altars/T5.png"), true));
		tier5AltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.tier5Altar")));
		entries.add(new EntryUniText(tier5AltarPages, "guide.BloodMagic.entryName.architect.tier5Altar"));
		
		ArrayList<IPage> priceOfPowerPages = new ArrayList();
		priceOfPowerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.priceOfPower")));
		entries.add(new EntryUniText(priceOfPowerPages, "guide.BloodMagic.entryName.architect.priceOfPower"));
		
		ArrayList<IPage> demonicOrbPages = new ArrayList();
		demonicOrbPages.add(new PageAltarRecipe(RecipeHolder.archmageBloodOrbRecipe));
		demonicOrbPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.demonicOrb")));
		entries.add(new EntryUniText(demonicOrbPages, "guide.BloodMagic.entryName.architect.demonicOrb"));
		
		ArrayList<IPage> energyBazookaPages = new ArrayList();
		demonicOrbPages.add(getOrbPageForRecipe(RecipeHolder.energyBazookaRecipe));
		energyBazookaPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.energyBazooka")));
		entries.add(new EntryUniText(energyBazookaPages, "guide.BloodMagic.entryName.architect.energyBazooka"));
		
		ArrayList<IPage> accelerationRunePages = new ArrayList();
		demonicOrbPages.add(getOrbPageForRecipe(RecipeHolder.accelerationRuneRecipe));
		accelerationRunePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.accelerationRune")));
		entries.add(new EntryUniText(accelerationRunePages, "guide.BloodMagic.entryName.architect.accelerationRune"));
		
		ArrayList<IPage> harvestPages = new ArrayList();
		demonicOrbPages.add(getOrbPageForRecipe(RecipeHolder.harvestSigilRecipe));
		harvestPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.harvest")));
		entries.add(new EntryUniText(harvestPages, "guide.BloodMagic.entryName.architect.harvest"));
		
		ArrayList<IPage> demonProblemPages = new ArrayList();
		demonProblemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.demonProblem")));
		entries.add(new EntryUniText(demonProblemPages, "guide.BloodMagic.entryName.architect.demonProblem"));
		
		ArrayList<IPage> tier6AltarPages = new ArrayList();
        tier6AltarPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/altars/T6.png"), true));
		tier6AltarPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.tier6Altar")));
		tier6AltarPages.add(new PageIRecipe(RecipeHolder.crystalCluserRecipe));
		tier6AltarPages.add(new PageAltarRecipe(RecipeHolder.transcendentBloodOrbRecipe));
		entries.add(new EntryUniText(tier6AltarPages, "guide.BloodMagic.entryName.architect.tier6Altar"));
		
		ArrayList<IPage> moreThanHumanPages = new ArrayList();
		moreThanHumanPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.architect.moreThanHuman")));
		entries.add(new EntryUniText(moreThanHumanPages, "guide.BloodMagic.entryName.architect.moreThanHuman"));
		
        categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.architect", new ItemStack(ModItems.divinationSigil)));
	}
	
	public static void registerRitualBook()
	{
		List<EntryAbstract> entries = new ArrayList();
		
//		ArrayList<IPage> testPages = new ArrayList();
//        testPages.add(PageRitualMultiBlock.getPageForRitual("AW031Convocation"));
//        entries.add(new EntryUniText(testPages, "Test page"));
        
		
		ArrayList<IPage> introPages = new ArrayList();
        introPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.intro")));
        entries.add(new EntryUniText(introPages, "guide.BloodMagic.entryName.rituals.intro"));
        
        ArrayList<IPage> weakRitualPages = new ArrayList();
        weakRitualPages.add(getOrbPageForRecipe(RecipeHolder.weakRitualStoneRecipe));
        weakRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.weakRitual")));
        entries.add(new EntryUniText(weakRitualPages, "guide.BloodMagic.entryName.rituals.weakRitual"));
        
        ArrayList<IPage> ritualsPages = new ArrayList();
        ritualsPages.add(getOrbPageForRecipe(RecipeHolder.ritualStoneRecipe));
        ritualsPages.add(getOrbPageForRecipe(RecipeHolder.masterStoneRecipe));
        ritualsPages.add(new PageAltarRecipe(RecipeHolder.waterScribeTool));
        ritualsPages.add(new PageAltarRecipe(RecipeHolder.fireScribeTool));
        ritualsPages.add(new PageAltarRecipe(RecipeHolder.earthScribeTool));
        ritualsPages.add(new PageAltarRecipe(RecipeHolder.airScribeTool));
        ritualsPages.add(new PageIRecipe(RecipeHolder.ritualDiviner1Recipe));
        ritualsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.rituals")));
        ritualsPages.add(new PageAltarRecipe(RecipeHolder.weakActivationRecipe));
        entries.add(new EntryUniText(ritualsPages, "guide.BloodMagic.entryName.rituals.rituals"));
        
        ArrayList<IPage> waterRitualPages = new ArrayList();
        waterRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Water.png"), true));
        waterRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.waterRitual")));
        entries.add(new EntryUniText(waterRitualPages, "guide.BloodMagic.entryName.rituals.waterRitual"));
        
        ArrayList<IPage> lavaRitualPages = new ArrayList();
        lavaRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Lava.png"), true));
        lavaRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.lavaRitual")));
        entries.add(new EntryUniText(lavaRitualPages, "guide.BloodMagic.entryName.rituals.lavaRitual"));
        
        ArrayList<IPage> groveRitualPages = new ArrayList();
        groveRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/GreenGrove.png"), true));
        groveRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.groveRitual")));
        entries.add(new EntryUniText(groveRitualPages, "guide.BloodMagic.entryName.rituals.groveRitual"));
        
        ArrayList<IPage> interdictionRitualPages = new ArrayList();
        interdictionRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Interdiction.png"), true));
        interdictionRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.interdictionRitual")));
        entries.add(new EntryUniText(interdictionRitualPages, "guide.BloodMagic.entryName.rituals.interdictionRitual"));
        
        ArrayList<IPage> containmentRitualPages = new ArrayList();
        containmentRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Containment.png"), true));
        containmentRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.containmentRitual")));
        entries.add(new EntryUniText(containmentRitualPages, "guide.BloodMagic.entryName.rituals.containmentRitual"));
        
        ArrayList<IPage> bindingRitualPages = new ArrayList();
        bindingRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Binding.png"), true));
        bindingRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.bindingRitual")));
        entries.add(new EntryUniText(bindingRitualPages, "guide.BloodMagic.entryName.rituals.bindingRitual"));
        
        ArrayList<IPage> beastModePages = new ArrayList();
        beastModePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.beastMode")));
        entries.add(new EntryUniText(beastModePages, "guide.BloodMagic.entryName.rituals.beastMode"));
        
        ArrayList<IPage> unbindingRitualPages = new ArrayList();
        unbindingRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Unbinding.png"), true));
        unbindingRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.unbindingRitual")));
        entries.add(new EntryUniText(unbindingRitualPages, "guide.BloodMagic.entryName.rituals.unbindingRitual"));
        
        ArrayList<IPage> jumpRitualPages = new ArrayList();
        jumpRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Jump.png"), true));
        jumpRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.jumpRitual")));
        entries.add(new EntryUniText(jumpRitualPages, "guide.BloodMagic.entryName.rituals.jumpRitual"));
        
        ArrayList<IPage> duskInkPages = new ArrayList();
        duskInkPages.add(new PageAltarRecipe(RecipeHolder.duskRecipe));
        duskInkPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.duskInk")));
        entries.add(new EntryUniText(duskInkPages, "guide.BloodMagic.entryName.rituals.duskInk"));
        
        ArrayList<IPage> magnetismRitualPages = new ArrayList();
        magnetismRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Magnetism.png"), true));
        magnetismRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.magnetismRitual")));
        entries.add(new EntryUniText(magnetismRitualPages, "guide.BloodMagic.entryName.rituals.magnetismRitual"));
        
        ArrayList<IPage> crusherRitualPages = new ArrayList();
        crusherRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Crusher.png"), true));
        crusherRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.crusherRitual")));
        entries.add(new EntryUniText(crusherRitualPages, "guide.BloodMagic.entryName.rituals.crusherRitual"));
        
        ArrayList<IPage> speedRitualPages = new ArrayList();
        speedRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Speed.png"), true));
        speedRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.speedRitual")));
        entries.add(new EntryUniText(speedRitualPages, "guide.BloodMagic.entryName.rituals.speedRitual"));
        
        ArrayList<IPage> shepherdRitualPages = new ArrayList();
        shepherdRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/AnimalGrowth.png"), true));
        shepherdRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.shepherdRitual")));
        entries.add(new EntryUniText(shepherdRitualPages, "guide.BloodMagic.entryName.rituals.shepherdRitual"));
        
        ArrayList<IPage> darkMagicPages = new ArrayList();
        darkMagicPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.darkMagic")));
        entries.add(new EntryUniText(darkMagicPages, "guide.BloodMagic.entryName.rituals.darkMagic"));
        
        ArrayList<IPage> knifeAndSufferingRitualPages = new ArrayList();
        knifeAndSufferingRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/WellOfSuffering.png"), true));
        knifeAndSufferingRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.knifeAndSufferingRitual.1")));
        knifeAndSufferingRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/FeatheredKnife.png"), true));
        knifeAndSufferingRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.knifeAndSufferingRitual.2")));
        entries.add(new EntryUniText(knifeAndSufferingRitualPages, "guide.BloodMagic.entryName.rituals.knifeAndSufferingRitual"));
        
        ArrayList<IPage> regenerationRitualPages = new ArrayList();
        regenerationRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Regeneration.png"), true));
        regenerationRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.regenerationRitual")));
        entries.add(new EntryUniText(regenerationRitualPages, "guide.BloodMagic.entryName.rituals.regenerationRitual"));
        
        ArrayList<IPage> harvestFestivalPages = new ArrayList();
        harvestFestivalPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.harvestFestival")));
        entries.add(new EntryUniText(harvestFestivalPages, "guide.BloodMagic.entryName.rituals.harvestFestival"));
        
        ArrayList<IPage> thenThereWereFivePages = new ArrayList();
        thenThereWereFivePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.thenThereWereFive")));
        entries.add(new EntryUniText(thenThereWereFivePages, "guide.BloodMagic.entryName.rituals.thenThereWereFive"));
        
        ArrayList<IPage> alchemyRitualPages = new ArrayList();
        alchemyRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Alchemy.png"), true));
        alchemyRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.alchemyRitual")));
        entries.add(new EntryUniText(alchemyRitualPages, "guide.BloodMagic.entryName.rituals.alchemyRitual"));
        
        ArrayList<IPage> domeRitualPages = new ArrayList();
        domeRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Dome.png"), true));
        domeRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.domeRitual")));
        entries.add(new EntryUniText(domeRitualPages, "guide.BloodMagic.entryName.rituals.domeRitual"));
        
        ArrayList<IPage> awakenedCrystalPages = new ArrayList();
        awakenedCrystalPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.awakenedCrystal")));
        entries.add(new EntryUniText(awakenedCrystalPages, "guide.BloodMagic.entryName.rituals.awakenedCrystal"));
        
        ArrayList<IPage> featheredEarthRitualPages = new ArrayList();
        featheredEarthRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/FeatheredEarth.png"), true));
        featheredEarthRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.featheredEarthRitual")));
        entries.add(new EntryUniText(featheredEarthRitualPages, "guide.BloodMagic.entryName.rituals.featheredEarthRitual"));
        
        ArrayList<IPage> gaiaRitualPages = new ArrayList();
        gaiaRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Gaia.png"), true));
        gaiaRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.gaiaRitual")));
        entries.add(new EntryUniText(gaiaRitualPages, "guide.BloodMagic.entryName.rituals.gaiaRitual"));
        
        ArrayList<IPage> condorRitualPages = new ArrayList();
        condorRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Flight.png"), true));
        condorRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.condorRitual")));
        entries.add(new EntryUniText(condorRitualPages, "guide.BloodMagic.entryName.rituals.condorRitual"));
        
        ArrayList<IPage> meteorRitualPages = new ArrayList();
        meteorRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Meteor.png"), true));
        meteorRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.meteorRitual")));
        entries.add(new EntryUniText(meteorRitualPages, "guide.BloodMagic.entryName.rituals.meteorRitual"));
        
        ArrayList<IPage> expulsionRitualPages = new ArrayList();
        expulsionRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Expulsion.png"), true));
        expulsionRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.expulsionRitual")));
        entries.add(new EntryUniText(expulsionRitualPages, "guide.BloodMagic.entryName.rituals.expulsionRitual"));
        
        ArrayList<IPage> costOfProgressPages = new ArrayList();
        costOfProgressPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.costOfProgress.1")));
        costOfProgressPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.costOfProgress.2")));
        entries.add(new EntryUniText(costOfProgressPages, "guide.BloodMagic.entryName.rituals.costOfProgress"));
        
        ArrayList<IPage> zephyrRitualPages = new ArrayList();
        zephyrRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Zephyr.png"), true));
        zephyrRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.zephyrRitual")));
        entries.add(new EntryUniText(zephyrRitualPages, "guide.BloodMagic.entryName.rituals.zephyrRitual"));
        
        ArrayList<IPage> harvestRitualPages = new ArrayList();
        harvestRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Harvest.png"), true));
        harvestRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.harvestRitual")));
        entries.add(new EntryUniText(harvestRitualPages, "guide.BloodMagic.entryName.rituals.harvestRitual"));
        
        ArrayList<IPage> eternalSoulRitualPages = new ArrayList();
        eternalSoulRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/EternalSoul.png"), true));
        eternalSoulRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.eternalSoulRitual")));
        entries.add(new EntryUniText(eternalSoulRitualPages, "guide.BloodMagic.entryName.rituals.eternalSoulRitual"));
        
        ArrayList<IPage> ellipsoidRitualPages = new ArrayList();
        ellipsoidRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Ellipsoid.png"), true));
        ellipsoidRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.ellipsoidRitual")));
        entries.add(new EntryUniText(ellipsoidRitualPages, "guide.BloodMagic.entryName.rituals.ellipsoidRitual"));
        
        ArrayList<IPage> evaporationRitualPages = new ArrayList();
        evaporationRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Evaporation.png"), true));
        evaporationRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.evaporationRitual")));
        entries.add(new EntryUniText(evaporationRitualPages, "guide.BloodMagic.entryName.rituals.evaporationRitual"));
        
        ArrayList<IPage> sacrosanctityRitualPages = new ArrayList();
        sacrosanctityRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Sacrosanctity.png"), true));
        sacrosanctityRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.sacrosanctityRitual")));
        entries.add(new EntryUniText(sacrosanctityRitualPages, "guide.BloodMagic.entryName.rituals.sacrosanctityRitual"));
        
        ArrayList<IPage> evilRitualPages = new ArrayList();
        evilRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/VeilOfEvil.png"), true));
        evilRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.evilRitual")));
        entries.add(new EntryUniText(evilRitualPages, "guide.BloodMagic.entryName.rituals.evilRitual"));
        
        ArrayList<IPage> stomachRitualPages = new ArrayList();
        stomachRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Stomach.png"), true));
        stomachRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.stomachRitual")));
        entries.add(new EntryUniText(stomachRitualPages, "guide.BloodMagic.entryName.rituals.stomachRitual"));
        
        ArrayList<IPage> reagentEffectsRitualPages = new ArrayList();
        for(int i=1; i<=24; i++)
        {
            reagentEffectsRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.reagentEffects." + i)));
        }
        entries.add(new EntryUniText(reagentEffectsRitualPages, "guide.BloodMagic.entryName.rituals.reagentEffects"));
        
        ArrayList<IPage> conclaveOfMagesPages = new ArrayList();
        conclaveOfMagesPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.conclaveOfMages")));
        entries.add(new EntryUniText(conclaveOfMagesPages, "guide.BloodMagic.entryName.rituals.conclaveOfMages"));
        
        ArrayList<IPage> forbiddenParadisePages = new ArrayList();
        forbiddenParadisePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.forbiddenParadise")));
        entries.add(new EntryUniText(forbiddenParadisePages, "guide.BloodMagic.entryName.rituals.forbiddenParadise"));
        
        ArrayList<IPage> convocationRitualPages = new ArrayList();
        convocationRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/Convocation.png"), true));
        convocationRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.convocationRitual")));
        entries.add(new EntryUniText(convocationRitualPages, "guide.BloodMagic.entryName.rituals.convocationRitual"));
        
        ArrayList<IPage> longHaulPages = new ArrayList();
        longHaulPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.longHaul")));
        entries.add(new EntryUniText(longHaulPages, "guide.BloodMagic.entryName.rituals.longHaul"));
        
        ArrayList<IPage> phantomHandsRitualPages = new ArrayList();
        phantomHandsRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/PhantomHands.png"), true));
        phantomHandsRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.phantomHandsRitual")));
        entries.add(new EntryUniText(phantomHandsRitualPages, "guide.BloodMagic.entryName.rituals.phantomHandsRitual"));
        
        ArrayList<IPage> anvilRitualPages = new ArrayList();
        anvilRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/BeatingAnvil.png"), true));
        anvilRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.anvilRitual")));
        entries.add(new EntryUniText(anvilRitualPages, "guide.BloodMagic.entryName.rituals.anvilRitual"));
        
        ArrayList<IPage> dawnInkPages = new ArrayList();
        dawnInkPages.add(new PageAltarRecipe(RecipeHolder.dawnRecipe));
        dawnInkPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.dawnInk")));
        entries.add(new EntryUniText(dawnInkPages, "guide.BloodMagic.entryName.rituals.dawnInk"));
        
        ArrayList<IPage> symmetryRitualPages = new ArrayList();
        symmetryRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/SymmetryOmega.png"), true));
        symmetryRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.symmetryRitual")));
        entries.add(new EntryUniText(symmetryRitualPages, "guide.BloodMagic.entryName.rituals.symmetryRitual"));
        
        ArrayList<IPage> stallingRitualPages = new ArrayList();
        stallingRitualPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/rituals/StallingOmega.png"), true));
        stallingRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.stallingRitual")));
        entries.add(new EntryUniText(stallingRitualPages, "guide.BloodMagic.entryName.rituals.stallingRitual"));
        
        ArrayList<IPage> newMoonRitualPages = new ArrayList();
        newMoonRitualPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.rituals.newMoonRitual")));
        entries.add(new EntryUniText(newMoonRitualPages, "guide.BloodMagic.entryName.rituals.newMoonRitual"));
		
		categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.rituals", new ItemStack(ModBlocks.blockMasterStone)));
	}
	
	public static void registerDemonBook()
	{
		List<EntryAbstract> entries = new ArrayList();
		
		ArrayList<IPage> ashesPages = new ArrayList();
        ashesPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.ashes")));
        entries.add(new EntryUniText(ashesPages, "guide.BloodMagic.entryName.demons.ashes"));
        
        ArrayList<IPage> tamedDemonPages = new ArrayList();
        tamedDemonPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.tamedDemon")));
        entries.add(new EntryUniText(tamedDemonPages, "guide.BloodMagic.entryName.demons.tamedDemon"));
        
        ArrayList<IPage> futurePages = new ArrayList();
        futurePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.future")));
        entries.add(new EntryUniText(futurePages, "guide.BloodMagic.entryName.demons.future"));
        
        ArrayList<IPage> knightPages = new ArrayList();
        knightPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.knight")));
        entries.add(new EntryUniText(knightPages, "guide.BloodMagic.entryName.demons.knight"));
        
        ArrayList<IPage> demonShardPages = new ArrayList();
        demonShardPages.addAll(PageHelper.pagesForLongText((StatCollector.translateToLocal("aw.entries.demons.demonShard")), new ItemStack(ModItems.demonBloodShard)));
        entries.add(new EntryUniText(demonShardPages, "guide.BloodMagic.entryName.demons.demonShard"));
       
		
		ArrayList<IPage> demonSummoningPages = new ArrayList();
		demonSummoningPages.add(new PageIRecipe(RecipeHolder.arcanePedestalRecipe));
		demonSummoningPages.add(new PageIRecipe(RecipeHolder.arcanePlinthRecipe));
        demonSummoningPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/demons/Ring1.png"), true));
        demonSummoningPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/demons/Ring2.png"), true));
        demonSummoningPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.demonSummoning")));
        entries.add(new EntryUniText(demonSummoningPages, "guide.BloodMagic.entryName.demons.demonSummoning"));
        
        ArrayList<IPage> keysGatePages = new ArrayList();
        for(int i=1; i<=10; i++)
        {
        	keysGatePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.keysGate." + i)));
        }
        entries.add(new EntryUniText(keysGatePages, "guide.BloodMagic.entryName.demons.keysGate"));
        
        ArrayList<IPage> futurePlansPages = new ArrayList();
        futurePlansPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.futurePlans")));
        entries.add(new EntryUniText(futurePlansPages, "guide.BloodMagic.entryName.demons.futurePlans"));
        
        ArrayList<IPage> demonInvasionPages = new ArrayList();
        demonInvasionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.demonInvasion")));
        entries.add(new EntryUniText(demonInvasionPages, "guide.BloodMagic.entryName.demons.demonInvasion"));
        
        ArrayList<IPage> observationsPages = new ArrayList();
        observationsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.demons.observations")));
        entries.add(new EntryUniText(observationsPages, "guide.BloodMagic.entryName.demons.observations"));        
        
		categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.demons", new ItemStack(ModItems.demonBloodShard)));
	}
	
	public static void registerSpellBook()
	{
		List<EntryAbstract> entries = new ArrayList();

		ArrayList<IPage> demonGirlPages = new ArrayList();
        demonGirlPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.demonGirl")));
        entries.add(new EntryUniText(demonGirlPages, "guide.BloodMagic.entryName.spells.demonGirl"));
        
        ArrayList<IPage> spellTablePages = new ArrayList();
        spellTablePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.spellTable.1")));
        spellTablePages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/SimpleSpellTable.png"), true));
        spellTablePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.spellTable.2")));
        entries.add(new EntryUniText(spellTablePages, "guide.BloodMagic.entryName.spells.spellTable"));
        
        ArrayList<IPage> simpleEffectsPages = new ArrayList();
        for(int i=1; i<=11; i++)
        {
            simpleEffectsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.simpleEffects." + i)));
        }
        entries.add(new EntryUniText(simpleEffectsPages, "guide.BloodMagic.entryName.spells.simpleEffects"));
        
        ArrayList<IPage> tableAndSkullsPages = new ArrayList();
		tableAndSkullsPages.add(new PageAltarRecipe(RecipeHolder.blankSpellRecipe));
        tableAndSkullsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.tableAndSkulls.1")));
		tableAndSkullsPages.add(getOrbPageForRecipe(RecipeHolder.spellTableRecipe));
        tableAndSkullsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.tableAndSkulls.2")));
        entries.add(new EntryUniText(tableAndSkullsPages, "guide.BloodMagic.entryName.spells.tableAndSkulls"));
        
        ArrayList<IPage> timePassesPages = new ArrayList();
        timePassesPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.timePasses")));
        entries.add(new EntryUniText(timePassesPages, "guide.BloodMagic.entryName.spells.timePasses"));
        
        ArrayList<IPage> complexSpellBasicsPages = new ArrayList();
        complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics.1")));
        complexSpellBasicsPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/Paradigm.png"), true));
        
        for(int i=2; i<=6; i++)
        	complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics." + i)));
        
        complexSpellBasicsPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/Effect.png"), true));
        complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics.7")));
        complexSpellBasicsPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/Modifier.png"), true));
        
        for(int i=8; i<=12; i++)
        	complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics." + i)));
        
        complexSpellBasicsPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/Enhancement.png"), true));
        
        for(int i=13; i<=16; i++)
        	complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics." + i)));
        
        complexSpellBasicsPages.add(new PageUnlocImage("", new ResourceLocation("alchemicalwizardry:textures/misc/screenshots/spells/Conduit.png"), true));
        complexSpellBasicsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellBasics.17")));

        entries.add(new EntryUniText(complexSpellBasicsPages, "guide.BloodMagic.entryName.spells.complexSpellBasics"));
        
        ArrayList<IPage> complexSpellEffectsPages = new ArrayList();
        for(int i=1; i<=17; i++)
        	complexSpellEffectsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.complexSpellEffects." + i)));
        entries.add(new EntryUniText(complexSpellEffectsPages, "guide.BloodMagic.entryName.spells.complexSpellEffects"));
        
        ArrayList<IPage> offTopicPages = new ArrayList();
        offTopicPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.offTopic")));
        entries.add(new EntryUniText(offTopicPages, "guide.BloodMagic.entryName.spells.offTopic"));
        
        ArrayList<IPage> demonicPowerPages = new ArrayList();
        demonicPowerPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.spells.demonicPower")));
        entries.add(new EntryUniText(demonicPowerPages, "guide.BloodMagic.entryName.spells.demonicPower"));
        
		categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.spells", new ItemStack(ModItems.itemComplexSpellCrystal)));
	}
	
	public static void registerAlchemyBook()
	{
		List<EntryAbstract> entries = new ArrayList();
		
		ArrayList<IPage> fatedMeetingPages = new ArrayList();
        fatedMeetingPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.fatedMeeting")));
        entries.add(new EntryUniText(fatedMeetingPages, "guide.BloodMagic.entryName.alchemy.fatedMeeting"));
        
        ArrayList<IPage> firstStepsPages = new ArrayList();
        firstStepsPages.add(getOrbPageForRecipe(RecipeHolder.alchemySetRecipe));
        firstStepsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.firstSteps")));
        entries.add(new EntryUniText(firstStepsPages, "guide.BloodMagic.entryName.alchemy.firstSteps"));
        
        ArrayList<IPage> chemistrySetPages = new ArrayList();
        for(int i=1; i<=10; i++)
        	chemistrySetPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.chemistrySet." + i)));
        entries.add(new EntryUniText(chemistrySetPages, "guide.BloodMagic.entryName.alchemy.chemistrySet"));
        
        ArrayList<IPage> incensePages = new ArrayList();
        incensePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.incense.1")));
        incensePages.add(new PageIRecipe(RecipeHolder.crucibleRecipe));
        incensePages.add(new PageIRecipe(RecipeHolder.woodAshRecipe));
        incensePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.incense.2")));
        incensePages.add(getOrbPageForRecipe(RecipeHolder.byrrusRecipe));
        incensePages.add(getOrbPageForRecipe(RecipeHolder.livensRecipe));
        incensePages.add(getOrbPageForRecipe(RecipeHolder.virRecipe));
        incensePages.add(getOrbPageForRecipe(RecipeHolder.purpuraRecipe));
        incensePages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.incense.3")));
        entries.add(new EntryUniText(incensePages, "guide.BloodMagic.entryName.alchemy.incense"));
        
        ArrayList<IPage> potionsPages = new ArrayList();
        potionsPages.add(new PageAltarRecipe(RecipeHolder.flaskRecipe));
        potionsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.potions")));
        entries.add(new EntryUniText(potionsPages, "guide.BloodMagic.entryName.alchemy.potions"));
        
        ArrayList<IPage> reagentRevolutionPages = new ArrayList();
        for(int i=1; i<=8; i++)
        	reagentRevolutionPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentRevolution." + i)));
        entries.add(new EntryUniText(reagentRevolutionPages, "guide.BloodMagic.entryName.alchemy.reagentRevolution"));
        
        ArrayList<IPage> newPotionsPages = new ArrayList();
        newPotionsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.newPotions")));
        entries.add(new EntryUniText(newPotionsPages, "guide.BloodMagic.entryName.alchemy.newPotions"));
        
        ArrayList<IPage> soulSandPages = new ArrayList();
        soulSandPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.soulSand")));
        entries.add(new EntryUniText(soulSandPages, "guide.BloodMagic.entryName.alchemy.soulSand"));
        
        ArrayList<IPage> timeGoesByPages = new ArrayList();
        timeGoesByPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.timeGoesBy")));
        entries.add(new EntryUniText(timeGoesByPages, "guide.BloodMagic.entryName.alchemy.timeGoesBy"));
        
        ArrayList<IPage> catalystsPages = new ArrayList();
        catalystsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.catalysts")));
        entries.add(new EntryUniText(catalystsPages, "guide.BloodMagic.entryName.alchemy.catalysts"));
        
        ArrayList<IPage> activationCrystalPages = new ArrayList();
        activationCrystalPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.activationCrystal")));
        entries.add(new EntryUniText(activationCrystalPages, "guide.BloodMagic.entryName.alchemy.activationCrystal"));
        
        ArrayList<IPage> reagentSystemPages = new ArrayList();
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.1")));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.calcinatorRecipe));
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.2")));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.belljarRecipe));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.relayRecipe));
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.3")));
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.4")));
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.5")));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.routerRecipe));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.segmenterRecipe));
        reagentSystemPages.add(new PageIRecipe(RecipeHolder.cleanserRecipe));
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.6")));
        
        reagentSystemPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.reagentSystem.7")));

        entries.add(new EntryUniText(reagentSystemPages, "guide.BloodMagic.entryName.alchemy.reagentSystem"));
        
        ArrayList<IPage> magusSecretPages = new ArrayList();
        magusSecretPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.magusSecret")));
        entries.add(new EntryUniText(magusSecretPages, "guide.BloodMagic.entryName.alchemy.magusSecret"));
        
        ArrayList<IPage> simpleCreationsPages = new ArrayList();
        simpleCreationsPages.addAll(PageHelper.pagesForLongText(StatCollector.translateToLocal("aw.entries.alchemy.simpleCreations")));
        entries.add(new EntryUniText(simpleCreationsPages, "guide.BloodMagic.entryName.alchemy.simpleCreations"));
		
		categories.add(new CategoryItemStack(entries, "guide.BloodMagic.category.alchemy", new ItemStack(ModItems.alchemyFlask)));
	}
}
