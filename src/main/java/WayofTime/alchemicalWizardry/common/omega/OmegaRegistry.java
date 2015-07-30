package WayofTime.alchemicalWizardry.common.omega;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaRegistry 
{
	public static HashMap<Reagent, OmegaParadigm> omegaList = new HashMap<Reagent, OmegaParadigm>();
	
	public static void registerParadigm(Reagent reagent, OmegaParadigm parad)
	{
		omegaList.put(reagent, parad);
	}
	
	public static OmegaParadigm getParadigmForReagent(Reagent reagent)
	{
		return omegaList.get(reagent);
	}
	
	public static boolean hasParadigm(Reagent reagent)
	{
		return omegaList.containsKey(reagent);
	}
	
	public static OmegaParadigm getOmegaParadigmOfWeilder(EntityPlayer player)
    {
    	ItemStack[] armours = player.inventory.armorInventory;
		
		ItemStack chestStack = armours[2];
		
		if(chestStack != null && chestStack.getItem() instanceof OmegaArmour)
		{
			return ((OmegaArmour)chestStack.getItem()).getOmegaParadigm();
		}
		
		return null;
    }
}
