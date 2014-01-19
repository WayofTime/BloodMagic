package joshie.alchemicalWizardy.nei;

import java.util.ArrayList;

import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.ModItems;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {	
	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new NEIAlchemyRecipeHandler());
		API.registerUsageHandler(new NEIAlchemyRecipeHandler());
		
		NEIAlchemyRecipeHandler.bloodOrbs = new ArrayList<Item>();
		NEIAlchemyRecipeHandler.bloodOrbs.add(ModItems.weakBloodOrb);
		NEIAlchemyRecipeHandler.bloodOrbs.add(ModItems.apprenticeBloodOrb);
		NEIAlchemyRecipeHandler.bloodOrbs.add(ModItems.magicianBloodOrb);
		NEIAlchemyRecipeHandler.bloodOrbs.add(ModItems.masterBloodOrb);
		NEIAlchemyRecipeHandler.bloodOrbs.add(ModItems.archmageBloodOrb);
	}

	@Override
	public String getName() {
		return "Blood Magic NEI";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
