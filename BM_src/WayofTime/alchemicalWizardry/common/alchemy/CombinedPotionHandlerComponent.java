package WayofTime.alchemicalWizardry.common.alchemy;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyPotionHelper;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;

public class CombinedPotionHandlerComponent 
{
	private int potionID;
	private int req1;
	private int req2;
	private int tickDuration;
	
	public CombinedPotionHandlerComponent(int potionID, int req1, int req2, int tickDuration)
	{
		this.potionID = potionID;
		this.req1 = req1;
		this.req2 = req2;
		this.tickDuration = tickDuration;
	}
	
	public boolean doesFlaskContentsMatch(ItemStack flaskStack)
	{
		ArrayList<AlchemyPotionHelper> list = AlchemyFlask.getEffects(flaskStack);
		
		boolean bool1 = false;
		boolean bool2 = false;
		
        if (list != null)
        {
            for (AlchemyPotionHelper aph : list)
            {
                if (aph.getPotionID() == req1)
                {
                    bool1 = true;
                }
                if (aph.getPotionID() == req2)
                {
                	bool2 = true;
                }
                if(bool1&&bool2)
                {
                	return true;
                }
            }
        }
        
		return false;
	}
}
