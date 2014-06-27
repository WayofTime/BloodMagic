package joshie.alchemicalWizardy.nei;

import java.util.ArrayList;

import net.minecraft.item.Item;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {	
	public static ArrayList<Item> bloodOrbs = new ArrayList<Item>();
	
	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new NEIAlchemyRecipeHandler());
		API.registerUsageHandler(new NEIAlchemyRecipeHandler());
		API.registerRecipeHandler(new NEIAltarRecipeHandler());
		API.registerUsageHandler(new NEIAltarRecipeHandler());
		API.registerRecipeHandler(new NEIBloodOrbShapedHandler());
		API.registerUsageHandler(new NEIBloodOrbShapedHandler());
		API.registerRecipeHandler(new NEIBloodOrbShapelessHandler());
		API.registerUsageHandler(new NEIBloodOrbShapelessHandler());
	}

	@Override
	public String getName() {
		return "Blood Magic NEI";
	}

	@Override
	public String getVersion() {
		return "1.2";
	}
}
