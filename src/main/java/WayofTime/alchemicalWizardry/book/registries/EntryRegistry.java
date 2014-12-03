package WayofTime.alchemicalWizardry.book.registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import WayofTime.alchemicalWizardry.book.compact.Category;
import WayofTime.alchemicalWizardry.book.compact.Entry;

public class EntryRegistry 
{
	public static void registerCategories(Category category)
	{
		categories.add(category);
		categoryMap.put(category.name, category);
		entryOrder.put(category, new ArrayList());
		categoryCount++;
	}
	public static ArrayList<Category> categories = new ArrayList<Category>();
	public static HashMap<Category, List<String>> entryOrder = new HashMap();
	public static HashMap<String, Category> categoryMap = new HashMap<String, Category>();
	
	public static int categoryCount = 0;

	public static void registerEntry(Category category, HashMap<String, Entry> entryMap, Entry entry)
	{
		entryMap.put(entry.name, entry);
		entries.put(category, entryMap);
		entryOrder.get(category).add(entry.name);
		
		if(maxEntries.containsKey(category) && entry.indexPage > maxEntries.get(category))
			maxEntries.put(category, entry.indexPage);
		else if(!maxEntries.containsKey(category))
			maxEntries.put(category, 0);

	}
	public static HashMap<Category, HashMap<String, Entry>> entries = new HashMap<Category, HashMap<String, Entry>>(); 

	public static HashMap<Category, Integer> maxEntries = new HashMap<Category, Integer>();
	
	
	public static HashMap<String, Entry> basics = new HashMap<String, Entry>();
	public static HashMap<String, Entry> rituals = new HashMap<String, Entry>();
	public static HashMap<String, Entry> bloodUtils = new HashMap<String, Entry>();

	public static Entry[] getEntriesInOrderForCategory(Category category)
	{
		HashMap<String, Entry> entries = EntryRegistry.entries.get(category);
		List<String> nameList = entryOrder.get(category);
		
		ArrayList<Entry> list = new ArrayList<Entry>();
		
		for(String str : nameList)
		{
			list.add(entries.get(str));
		}
		
		Object[] entriesList = list.toArray();
		Entry[] entryList = new Entry[entriesList.length];
		
		for(int i=0; i<entriesList.length; i++)
		{
			entryList[i] = (Entry)(entriesList[i]);
		}
		
		return entryList;
	}
}