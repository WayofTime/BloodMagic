package WayofTime.alchemicalWizardry.api.sacrifice;

import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerSacrificeHandler 
{
	public int getPlayerIncense(EntityPlayer player)
	{
		return APISpellHelper.getCurrentIncense(player);
	}
	
	public void setPlayerIncense(EntityPlayer player, int amount)
	{
		APISpellHelper.setCurrentIncense(player, amount);
	}
	
	public boolean incrementIncense(EntityPlayer player, int min, int max)
	{
		int amount = this.getPlayerIncense(player);
		if(amount < min || amount >= max)
		{
			return false;
		}
		
		amount++;
		
		return true;
	}
	
	public boolean sacrificePlayerHealth(EntityPlayer player)
	{
		
		return false;
	}
}
