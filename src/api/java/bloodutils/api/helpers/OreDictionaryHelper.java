package bloodutils.api.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {
	public static boolean entryExists(String material){
		return OreDictionary.getOres(material).size() > 0;
	}
	
	public static ItemStack getItemStack(String material, int entry){
		if(entryExists(material))
			return OreDictionary.getOres(material).get(entry);
		return null;
	}
}