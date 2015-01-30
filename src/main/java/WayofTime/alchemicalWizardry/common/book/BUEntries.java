package WayofTime.alchemicalWizardry.common.book;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.book.compact.Category;
import WayofTime.alchemicalWizardry.book.compact.Entry;
import WayofTime.alchemicalWizardry.book.entries.EntryImage;
import WayofTime.alchemicalWizardry.book.entries.EntryItemText;
import WayofTime.alchemicalWizardry.book.entries.EntryRitualInfo;
import WayofTime.alchemicalWizardry.book.entries.EntryText;
import WayofTime.alchemicalWizardry.book.entries.IEntry;
import WayofTime.alchemicalWizardry.book.enums.EnumType;
import WayofTime.alchemicalWizardry.book.registries.EntryRegistry;

public class BUEntries
{
	public void postInit()
	{
		initCategories();
		initEntries();	
	}
	
	public void initCategories(){
		categoryBasics = new Category("Basics", new ItemStack(ModItems.weakBloodOrb), EnumType.ITEM);
		categoryRituals = new Category("Rituals", new ItemStack(ModItems.itemRitualDiviner), EnumType.ITEM);
		catagoryArchitect = new Category("The Architect", new ItemStack(ModBlocks.blockAltar), EnumType.BLOCK);

		registerCategories();
	}
	public static Category categoryBasics;
	public static Category categoryRituals;
	public static Category catagoryArchitect;
	
	public void registerCategories(){
		EntryRegistry.registerCategories(BUEntries.categoryBasics);
		EntryRegistry.registerCategories(BUEntries.categoryRituals);
		EntryRegistry.registerCategories(BUEntries.catagoryArchitect);
	}
	
	public void initEntries()
	{
		HashMap<Integer, IEntry> aIntroMap = new HashMap();
		aIntroMap.put(0, new EntryImage("bloodutils:textures/misc/screenshots/t1.png", 854, 480));
		/* The Architect */
		aIntro = this.getMixedTextEntry(6, "Your classic tragic backstory", 1, aIntroMap);
		aBloodAltar = this.getPureTextEntry(3, "The Blood Altar", 1);
		aSoulNetwork = this.getPureTextEntry(3, "The Soul Network", 1);
		aBasicsOfSigils = this.getPureTextEntry(4, "Basics of sigils and a glimpse into the soul", 1);
		aTrainingAndWaterSigil = this.getPureTextEntry(6, "Training, and water sigils", 1);
		aLavaCrystal = this.getPureTextEntry(5, "The Lava crystal", 1);
		aLavaSigil = this.getPureTextEntry(8, "The perversion of the Nether, and a sigil of lava", 1);
		aBlankRunes = this.getPureTextEntry(5, "Blank runes, the building blocks of the future", 1);
		aSpeedRune = this.getPureTextEntry(2, "Speed runes", 1);
		aApprenticeOrb = this.getPureTextEntry(4, "A shining green orb", 1);
		aVoidSigil = this.getPureTextEntry(2, "The void sigil", 1);
		aAirSigil = this.getPureTextEntry(2, "Air sigil", 1);
		aSightSigil = this.getPureTextEntry(2, "Sigil of Sight", 1);
		aAdvancedAltar = this.getPureTextEntry(5, "Advanced altar mechanics", 1);
		aFastMinder = this.getPureTextEntry(2, "Sigil of the fast miner", 2);
		aSoulFray = this.getPureTextEntry(2, "Soul Fray, a few thin threads", 2);
		aGreenGrove = this.getPureTextEntry(2, "Green grove, a farmers friend", 2);
		aDaggerOfSacrifice = this.getPureTextEntry(9, "Dagger of sacrifice, an alternative energy source", 2);
		aRunesOfSacrifice = this.getPureTextEntry(10, "Runes of Sacrifice", 2);
		aBloodLetterPack = this.getPureTextEntry(4, "The blood letters pack", 2);
		aNewFriends = this.getPureTextEntry(18, "And then there was five", 2);
		aUpgradedAltar = this.getPureTextEntry(4, "An altar upgraded, and orb formed", 2);
		aNewRunes = this.getPureTextEntry(5, "New runes", 2);
		aPhandomBridge = this.getPureTextEntry(8, "The Phantom bridge", 2);
		aHolding = this.getPureTextEntry(2, "Sigil of holding", 2);
		aElementalAffinity = this.getPureTextEntry(2, "Elemental affinity, a spell casters best friend", 2);
		aRitualStones = this.getPureTextEntry(2, "Recreating ritual stones", 2);
		aBloodLamp = this.getPureTextEntry(3, "Shining a blood lamp sigil", 2);
		aBoundArmour = this.getPureTextEntry(8, "Bound armor, the walking fortress", 3);
		aSanguineArmour = this.getPureTextEntry(2, "Sanguine armor", 3);
		aSoulSuppressing = this.getPureTextEntry(1, "Suppressing the soul", 3);
		aRitualDiviner = this.getPureTextEntry(5, "The ritual diviner", 3);
		aBloodShards = this.getPureTextEntry(5, "Blood shards", 3);
		aLifeOfMage = this.getPureTextEntry(9, "he life of a Mage", 3);
		aT4 = this.getPureTextEntry(5, "The T4 altar, and a master's orb", 3);
		aSigilOfWhirlwind = this.getPureTextEntry(2, "The sigil of whirlwinds", 3);
		aSigilOfCompression = this.getPureTextEntry(2, "The sigil of compression", 3);
		aEnderDivergence = this.getPureTextEntry(3, "The Ender divergence", 3);
		aTeleposer = this.getPureTextEntry(9, "The Teleposer", 3);
		aSuppression = this.getPureTextEntry(2, "The sigil of suppression", 3);
		aSuperiorCapacity = this.getPureTextEntry(2, "The superior capacity rune", 3);
		aRuneOfOrb = this.getPureTextEntry(3, "The rune of the orb", 3);
		aFieldTrip = this.getPureTextEntry(19, "A field trip", 4);
		aKeyOfBinding = this.getPureTextEntry(4, "The key of binding", 4);
		aT5 = this.getPureTextEntry(4, "The trials of a T5 altar", 4);
		aPriceOfPower = this.getPureTextEntry(2, "The price of power", 4);
		aDemonicOrb = this.getPureTextEntry(1, "Demonic orb", 4);
		aEnergyBazooka = this.getPureTextEntry(2, "The unspeakable power of the energy bazooka", 4);
		aAccelerationRune = this.getPureTextEntry(2, "Acceleration runes", 4);
		aHarvestSigil = this.getPureTextEntry(3, "The sigil of the Harvest goddess", 4);
		aDemons = this.getPureTextEntry(6, "Solving a demon problem with more demons", 4);
		aT6 = this.getPureTextEntry(3, "The T6 altar already", 4);
		
		rIntro = new Entry(new IEntry[]{new EntryText(), new EntryText(), new EntryText()}, "Introduction", 1);
		rWeakRituals = new Entry(new IEntry[]{new EntryText(), new EntryText(), new EntryText(), new EntryText()}, "Weak Rituals", 1);
		rRituals = new Entry(new IEntry[]{new EntryText(), new EntryText(), new EntryText(), new EntryText()}, "Rituals", 1);
		rRitualWater = this.getPureTextEntry(2, "Ritual of the Full Spring", 1);
		
		theAltar = new Entry(new IEntry[]{new EntryItemText(new ItemStack(ModBlocks.blockAltar), "Blood Altar")}, EnumChatFormatting.BLUE + "Blood Altar", 1);
		runes = new Entry(new IEntry[]{new EntryItemText(new ItemStack(ModBlocks.runeOfSelfSacrifice)), new EntryItemText(new ItemStack(ModBlocks.runeOfSacrifice)), new EntryItemText(new ItemStack(ModBlocks.speedRune))}, "Runes", 1);
		
		/** Page 1 */
		ritualWater = new Entry(new IEntry[]{new EntryText()}, "Full Spring", 1);
		ritualLava = new Entry(new IEntry[]{new EntryText(), new EntryText()}, "Nether", 1);
		ritualGreenGrove = new Entry(new IEntry[]{new EntryText(), new EntryText()}, "Green Grove", 1);
		ritualInterdiction = new Entry(new IEntry[]{new EntryText(), new EntryText()}, "Interdiction", 1);
		ritualContainment = new Entry(new IEntry[]{new EntryText()}, "Containment", 1);
		ritualHighJump = new Entry(new IEntry[]{new EntryText()}, "High Jump", 1);
		ritualSpeed = new Entry(new IEntry[]{new EntryText()}, "Speed", 1);
		ritualMagnet = new Entry(new IEntry[]{new EntryText()}, "Magnetism", 1);
		ritualCrusher = new Entry(new IEntry[]{new EntryText()}, "Crusher", 1);
		ritualShepherd = new Entry(new IEntry[]{new EntryText()}, "Shepherd", 1);
		ritualRegeneration = new Entry(new IEntry[]{new EntryText(), new EntryText(), new EntryText()}, "Regeneration", 1);
		ritualFeatheredKnife = new Entry(new IEntry[]{new EntryText(), new EntryText(), new EntryText(), new EntryText()}, "Feathered Knife", 1);
		ritualMoon = new Entry(new IEntry[]{new EntryText()}, "Harvest Moon", 1);
		ritualSoul = new Entry(new IEntry[]{new EntryText(), new EntryText()}, "Eternal Soul", 2);
		
		ritualCure = new Entry(new IEntry[]{new EntryText(), new EntryRitualInfo(500)}, "Curing", 1);
//		blockDivination = new Entry(new IEntry[]{new EntryItemText(new ItemStack(BUBlocks.altarProgress)), new EntryCraftingRecipe(BURecipes.altarProgress)}, "Divination Block", 1);
//		sigilAdvancedDivination = new Entry(new IEntry[]{new EntryItemText(new ItemStack(BUItems.sigil_advancedDivination)), new EntryAltarRecipe(BURecipes.advancedSigil)}, "Advanced Divination", 1);
//		
//		elementRituals = new Entry(new IEntry[]{new EntryItemText(new ItemStack(BUBlocks.darknessArea)), new EntryText(), new EntryCraftingRecipe(BURecipes.gemEmpty), new EntryAltarRecipe(BURecipes.diamondBlood)}, "Elemental Rituals", 1);
//		reviving = new Entry(new IEntry[]{new EntryText(), new EntryCraftingRecipe(BURecipes.reviver)}, "Reviving", 1);
		
		/** Debug */
		debug = new Entry(new IEntry[]{new EntryText("Debug"), new EntryImage("bloodutils:textures/misc/screenshots/t1.png", 854, 480, "Debug")}, EnumChatFormatting.AQUA + "De" + EnumChatFormatting.RED + "bug", 1);
		registerEntries();
	}
	
	public Entry getPureTextEntry(int numberOfPages, String name, int pageNumber)
	{
		IEntry[] entries = new IEntry[numberOfPages];
		for(int i=0; i<numberOfPages; i++)
		{
			entries[i] = new EntryText();
		}
		return new Entry(entries, name, pageNumber);
	}
	
	public Entry getMixedTextEntry(int numberOfPages, String name, int pageNumber, Map<Integer, IEntry> map)
	{
		IEntry[] entries = new IEntry[numberOfPages];
		for(int i=0; i<numberOfPages; i++)
		{
			entries[i] = new EntryText();
		}
		
		for(Map.Entry<Integer, IEntry> ent : map.entrySet())
		{
			if(ent.getKey() < entries.length)
			{
				entries[ent.getKey()] = ent.getValue();
			}
		}
		
		return new Entry(entries, name, pageNumber);
	}
	
	/* Architect */
	public static Entry aIntro;
	public static Entry aBloodAltar;
	public static Entry aSoulNetwork;
	public static Entry aBasicsOfSigils;
	public static Entry aTrainingAndWaterSigil;
	public static Entry aLavaCrystal;
	public static Entry aLavaSigil;
	public static Entry aBlankRunes;
	public static Entry aSpeedRune;
	public static Entry aApprenticeOrb;
	public static Entry aVoidSigil;
	public static Entry aAirSigil;
	public static Entry aSightSigil;
	public static Entry aAdvancedAltar;
	public static Entry aFastMinder;
	public static Entry aSoulFray;
	public static Entry aGreenGrove;
	public static Entry aDaggerOfSacrifice;
	public static Entry aRunesOfSacrifice;
	public static Entry aBloodLetterPack;
	public static Entry aNewFriends;
	public static Entry aUpgradedAltar;
	public static Entry aNewRunes;
	public static Entry aPhandomBridge;
	public static Entry aHolding;
	public static Entry aElementalAffinity;
	public static Entry aRitualStones;
	public static Entry aBloodLamp;
	public static Entry aBoundArmour;
	public static Entry aSanguineArmour;
	public static Entry aSoulSuppressing;
	public static Entry aRitualDiviner;
	public static Entry aBloodShards;
	public static Entry aLifeOfMage;
	public static Entry aT4;
	public static Entry aSigilOfWhirlwind;
	public static Entry aSigilOfCompression;
	public static Entry aEnderDivergence;
	public static Entry aTeleposer;
	public static Entry aSuppression;
	public static Entry aSuperiorCapacity;
	public static Entry aRuneOfOrb;
	public static Entry aFieldTrip;
	public static Entry aKeyOfBinding;
	public static Entry aT5;
	public static Entry aPriceOfPower;
	public static Entry aDemonicOrb;
	public static Entry aEnergyBazooka;
	public static Entry aAccelerationRune;
	public static Entry aHarvestSigil;
	public static Entry aDemons;
	public static Entry aT6;
	
	public static Entry rIntro;
	public static Entry rWeakRituals;
	public static Entry rRituals;
	public static Entry rRitualWater;
	
	public static Entry theAltar;
	public static Entry runes;
	
	public static Entry ritualCure;
	public static Entry sigilAdvancedDivination;
	public static Entry blockDivination;

	public static Entry ritualWater;
	public static Entry ritualLava;
	public static Entry ritualGreenGrove;
	public static Entry ritualInterdiction;
	public static Entry ritualContainment;
	public static Entry ritualHighJump;
	public static Entry ritualSpeed;
	public static Entry ritualMagnet;
	public static Entry ritualCrusher;
	public static Entry ritualShepherd;
	public static Entry ritualRegeneration;
	public static Entry ritualFeatheredKnife;
	public static Entry ritualMoon;
	public static Entry ritualSoul;
	
	public static Entry elementRituals;
	public static Entry reviving;

	public static Entry debug;
	
	public void registerEntries()
	{
		/* Architect */
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aIntro);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBloodAltar);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSoulNetwork);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBasicsOfSigils);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aTrainingAndWaterSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aLavaCrystal);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aLavaSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBlankRunes);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSpeedRune);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aApprenticeOrb);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aVoidSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aAirSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSightSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aAdvancedAltar);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aFastMinder);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSoulFray);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aGreenGrove);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aDaggerOfSacrifice);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aRunesOfSacrifice);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBloodLetterPack);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aNewFriends);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aUpgradedAltar);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aNewRunes);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aPhandomBridge);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aHolding);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aElementalAffinity);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aRitualStones);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBloodLamp);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBoundArmour);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSanguineArmour);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSoulSuppressing);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aRitualDiviner);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aBloodShards);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aLifeOfMage);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aT4);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSigilOfWhirlwind);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSigilOfCompression);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aEnderDivergence);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aTeleposer);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSuppression);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aSuperiorCapacity);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aRuneOfOrb);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aFieldTrip);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aKeyOfBinding);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aT5);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aPriceOfPower);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aDemonicOrb);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aEnergyBazooka);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aAccelerationRune);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aHarvestSigil);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aDemons);
		EntryRegistry.registerEntry(BUEntries.catagoryArchitect, EntryRegistry.architect, BUEntries.aT6);
		
		EntryRegistry.registerEntry(BUEntries.categoryBasics, EntryRegistry.basics, BUEntries.theAltar);
		EntryRegistry.registerEntry(BUEntries.categoryBasics, EntryRegistry.basics, BUEntries.runes);
		
		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.rIntro);
		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.rWeakRituals);
		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.rRituals);
		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.rRitualWater);		
		
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualWater);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualLava);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualGreenGrove);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualInterdiction);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualContainment);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualHighJump);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualSpeed);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualMagnet);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualCrusher);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualShepherd);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualRegeneration);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualFeatheredKnife);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualMoon);
//		EntryRegistry.registerEntry(BUEntries.categoryRituals, EntryRegistry.rituals, BUEntries.ritualSoul);

		/** Debug */
		EntryRegistry.registerEntry(BUEntries.categoryBasics, EntryRegistry.basics, BUEntries.debug);
	}
}