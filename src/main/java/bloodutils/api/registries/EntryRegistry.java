package bloodutils.api.registries;

import java.util.ArrayList;
import java.util.HashMap;

import bloodutils.api.compact.Category;
import bloodutils.api.compact.Entry;
import bloodutils.api.entries.IEntry;

public class EntryRegistry {
	public static void registerCategories(Category category){
		categories.add(category);
		categoryMap.put(category.name, category);
		categoryCount++;
	}
	public static ArrayList<Category> categories = new ArrayList<Category>();
	public static HashMap<String, Category> categoryMap = new HashMap<String, Category>();
	
	public static int categoryCount = 0;

	public static void registerEntry(Category category, HashMap<String, Entry> entryMap, Entry entry){
		entryMap.put(entry.name, entry);
		entries.put(category, entryMap);
		
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

}