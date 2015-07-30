package WayofTime.alchemicalWizardry.common.demonVillage.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import WayofTime.alchemicalWizardry.ModItems;

public class DemonVillageLootRegistry 
{
	public static ArrayList<WeightedRandomChestContent> list1 = new ArrayList<WeightedRandomChestContent>();
	
	public static void init()
	{
		String[] tier1Strings = new String[]{ChestGenHooks.DUNGEON_CHEST, ChestGenHooks.PYRAMID_DESERT_CHEST};
		for(String str : tier1Strings)
		{
			List<WeightedRandomChestContent> contents = ChestGenHooks.getItems(str, new Random());
			if(contents != null)
			{
				for(WeightedRandomChestContent content : contents)
				{
					list1.add(content);
				}
			}
		}
		
		list1.add(new WeightedRandomChestContent(ModItems.baseItems, 28, 1, 2, 5));
		list1.add(new WeightedRandomChestContent(ModItems.baseItems, 29, 1, 2, 5));
	}
	
	public static void populateChest(IInventory tile, int tier)
	{
		WeightedRandomChestContent.generateChestContents(new Random(), list1, tile, tile.getSizeInventory() / 3);
	}
}
