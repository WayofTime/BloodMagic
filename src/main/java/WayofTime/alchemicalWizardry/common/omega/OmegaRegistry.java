package WayofTime.alchemicalWizardry.common.omega;

import java.util.HashMap;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;

public class OmegaRegistry 
{
	public static HashMap<Reagent, OmegaParadigm> omegaList = new HashMap();
	
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
}
