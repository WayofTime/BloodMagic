package WayofTime.alchemicalWizardry.common.harvest;

import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;

public class PamHarvestCompatRegistry 
{
	public static void registerPamHandlers()
	{
		registerSeededHandler("blackberry", 7);
		registerSeededHandler("blueberry", 7);
		registerSeededHandler("candleberry", 7);
		registerSeededHandler("raspberry", 7);
		registerSeededHandler("strawberry", 7);
		registerSeededHandler("cactusfruit", 7);
		registerSeededHandler("asparagus", 7);
		registerSeededHandler("barley", 7);
		registerSeededHandler("oats", 7);
		registerSeededHandler("rye", 7);
		registerSeededHandler("corn", 7);
		registerSeededHandler("bambooshoot", 7);
		registerSeededHandler("cantaloupe", 7);
		registerSeededHandler("cucumber", 7);
		registerSeededHandler("windersquash", 7);
		registerSeededHandler("zucchini", 7);
		registerSeededHandler("beat", 7);
		registerSeededHandler("onion", 7);
		registerSeededHandler("parsnip", 7);
		registerSeededHandler("peanut", 7);
		registerSeededHandler("radish", 7);
		registerSeededHandler("rutabaga", 7);
		registerSeededHandler("sweetpotato", 7);
		registerSeededHandler("turnip", 7);
		registerSeededHandler("rhubarb", 7);
		registerSeededHandler("celery", 7);
		registerSeededHandler("garlic", 7);
		registerSeededHandler("ginger", 7);
		registerSeededHandler("spiceleaf", 7);
		registerSeededHandler("tealeaf", 7);
		registerSeededHandler("coffeebean", 7);
		registerSeededHandler("mustardseeds", 7);
		registerSeededHandler("brocolli", 7);
		registerSeededHandler("cauliflower", 7);
		registerSeededHandler("leek", 7);
		registerSeededHandler("lettuce", 7);
		registerSeededHandler("scallion", 7);
		registerSeededHandler("artichoke", 7);
		registerSeededHandler("brusselsprout", 7);
		registerSeededHandler("cabbage", 7);
		registerSeededHandler("whitemushroom", 7);
		registerSeededHandler("bean", 7);
		registerSeededHandler("soybean", 7);
		registerSeededHandler("bellpepper", 7);
		registerSeededHandler("chili", 7);
		registerSeededHandler("eggplant", 7);
		registerSeededHandler("pamokra", 7);
		registerSeededHandler("peas", 7);
		registerSeededHandler("tomato", 7);
		registerSeededHandler("cotton", 7);
		registerSeededHandler("pineapple", 7);
		registerSeededHandler("grape", 7);
		registerSeededHandler("kiwi", 7);
		registerSeededHandler("cranberry", 7);
		registerSeededHandler("rice", 7);
		registerSeededHandler("seaweed", 7);

		registerFruitHandler("apple", 7, 0);
		registerFruitHandler("Almond", 7, 0);
		registerFruitHandler("Apricot", 7, 0);
		registerFruitHandler("Avocado", 7, 0);
		registerFruitHandler("Banana", 7, 0);
		registerFruitHandler("Cashew", 7, 0);
		registerFruitHandler("Cherry", 7, 0);
		registerFruitHandler("Chestnut", 7, 0);
		registerFruitHandler("Cinnamon", 7, 0);
		registerFruitHandler("Coconut", 7, 0);
		registerFruitHandler("Date", 7, 0);
		registerFruitHandler("Dragonfruit", 7, 0);
		registerFruitHandler("Durian", 7, 0);
		registerFruitHandler("Fig", 7, 0);
		registerFruitHandler("Grapefruit", 7, 0);
		registerFruitHandler("Lemon", 7, 0);
		registerFruitHandler("Lime", 7, 0);
		registerFruitHandler("Maple", 7, 0);
		registerFruitHandler("Mango", 7, 0);
		registerFruitHandler("Nutmeg", 7, 0);
		registerFruitHandler("Olive", 7, 0);
		registerFruitHandler("Orange", 7, 0);
		registerFruitHandler("Papaya", 7, 0);
		registerFruitHandler("Paperbark", 7, 0);
		registerFruitHandler("Peach", 7, 0);
		registerFruitHandler("Pear", 7, 0);
		registerFruitHandler("Pecan", 7, 0);
		registerFruitHandler("Peppercorn", 7, 0);
		registerFruitHandler("Persimmon", 7, 0);
		registerFruitHandler("Pistachio", 7, 0);
		registerFruitHandler("Plum", 7, 0);
		registerFruitHandler("Pomegranate", 7, 0);
		registerFruitHandler("Starfruit", 7, 0);
		registerFruitHandler("Vanillabean", 7, 0);
		registerFruitHandler("Walnut", 7, 0);
	}
	
	public static void registerSeededHandler(String name, int meta)
	{
		String block = "harvestcraft:pam" + name + "Crop";
		String seed = "harvestcraft:" + name + "Item";
		
		GenericSeededHarvestHandler handler = new GenericSeededHarvestHandler(block, meta, seed);
		if(handler.isHarvesterValid())
		{
			HarvestRegistry.registerHarvestHandler(handler);
		}
	}
	
	public static void registerFruitHandler(String name, int harvestMeta, int resetMeta)
	{
		String block = "harvestcraft:pam" + name;
		
		GenericPamSeedlessFruitHarvestHandler handler = new GenericPamSeedlessFruitHarvestHandler(block, harvestMeta, resetMeta);
		if(handler.isHarvesterValid())
		{
			HarvestRegistry.registerHarvestHandler(handler);
		}
	}
}
