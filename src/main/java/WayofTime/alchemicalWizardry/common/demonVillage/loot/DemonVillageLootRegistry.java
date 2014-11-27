package WayofTime.alchemicalWizardry.common.demonVillage.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.WeightedRandomChestContent;

public class DemonVillageLootRegistry 
{
	public static ArrayList<WeightedRandomChestContent> list = new ArrayList();
	
	static
	{
		list.add(new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10));
	}
	
	public static void populateChest(IInventory tile, int tier)
	{
		WeightedRandomChestContent.generateChestContents(new Random(), toArray(list), tile, 10);
	}
	
	public static WeightedRandomChestContent[] toArray(List<WeightedRandomChestContent> aList)
	{
		int size = aList.size();
		WeightedRandomChestContent[] contents = new WeightedRandomChestContent[size];
		
		contents = aList.toArray(contents);
		
		return contents;
	}
}
