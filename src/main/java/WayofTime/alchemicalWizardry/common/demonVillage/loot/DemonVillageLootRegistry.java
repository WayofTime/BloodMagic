package WayofTime.alchemicalWizardry.common.demonVillage.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import WayofTime.alchemicalWizardry.ModItems;

public class DemonVillageLootRegistry 
{
	public static ArrayList<WeightedRandomChestContent> list1 = new ArrayList();
	
	public static void init()
	{
		ItemStack lifeShardStack = new ItemStack(ModItems.baseItems, 1, 28);
        ItemStack soulShardStack = new ItemStack(ModItems.baseItems, 1, 29);
        
		String[] tier1Strings = new String[]{ChestGenHooks.DUNGEON_CHEST, ChestGenHooks.PYRAMID_DESERT_CHEST};
		for(String str : tier1Strings)
		{
			WeightedRandomChestContent[] contents = ChestGenHooks.getItems(str, new Random());
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
		WeightedRandomChestContent.generateChestContents(new Random(), toArray(list1), tile, tile.getSizeInventory() / 3);
	}
	
	public static WeightedRandomChestContent[] toArray(List<WeightedRandomChestContent> aList)
	{
		int size = aList.size();
		WeightedRandomChestContent[] contents = new WeightedRandomChestContent[size];
		
		contents = aList.toArray(contents);
		
		return contents;
	}
}
