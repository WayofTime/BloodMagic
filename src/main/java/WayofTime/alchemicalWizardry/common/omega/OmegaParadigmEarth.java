package WayofTime.alchemicalWizardry.common.omega;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigmEarth extends OmegaParadigm
{
	public OmegaParadigmEarth(OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots) 
	{
		super(ReagentRegistry.terraeReagent,  helmet, chestPiece, leggings, boots, new ReagentRegenConfiguration(50, 10, 100));
	}
	
//	@Override
//	public float getCostPerTickOfUse(EntityPlayer player)
//	{
//		if(player.isInWater())
//		{
//			return 0.5f;
//		}else
//		{
//			return 1;
//		}
//	}
}
