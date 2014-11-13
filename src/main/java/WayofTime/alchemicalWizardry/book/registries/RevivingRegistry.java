package WayofTime.alchemicalWizardry.book.registries;

import java.util.HashMap;

import WayofTime.alchemicalWizardry.book.compact.CompactItem;
import WayofTime.alchemicalWizardry.book.interfaces.IReviving;

public class RevivingRegistry {
	public static void registerReviving(CompactItem ingredients, IReviving reviving){
		recipes.put(ingredients, reviving);
	}
	public static HashMap<CompactItem, IReviving> recipes = new HashMap<CompactItem, IReviving>();
}