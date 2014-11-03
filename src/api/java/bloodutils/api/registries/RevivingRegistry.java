package bloodutils.api.registries;

import java.util.HashMap;

import bloodutils.api.compact.CompactItem;
import bloodutils.api.interfaces.IReviving;

public class RevivingRegistry {
	public static void registerReviving(CompactItem ingredients, IReviving reviving){
		recipes.put(ingredients, reviving);
	}
	public static HashMap<CompactItem, IReviving> recipes = new HashMap<CompactItem, IReviving>();
}