package WayofTime.alchemicalWizardry.api.sacrifice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;

public class PlayerSacrificeHandler 
{
	public static float scalingOfSacrifice = 0.001f;
	public static int soulFrayDuration = 400;
	public static float getPlayerIncense(EntityPlayer player)
	{
		return APISpellHelper.getCurrentIncense(player);
	}
	
	public static void setPlayerIncense(EntityPlayer player, float amount)
	{
		APISpellHelper.setCurrentIncense(player, amount);
	}
	
	public static boolean incrementIncense(EntityPlayer player, float min, float max, float increment)
	{
		float amount = getPlayerIncense(player);
		if(amount < min || amount >= max)
		{
			return false;
		}
		
		amount = amount + Math.min(increment, max - amount);
		setPlayerIncense(player, amount);
		
//		System.out.println("Amount of incense: " + amount + ", Increment: " + increment);
		
		return true;
	}
	
	public static boolean sacrificePlayerHealth(EntityPlayer player)
	{
		if(player.isPotionActive(AlchemicalWizardry.customPotionSoulFray))
		{
			return false;
		}
		
		float amount = getPlayerIncense(player);
		
		if(amount >= 0)
		{
			float health = player.getHealth();
			float maxHealth = player.getMaxHealth();
						
			if(health > maxHealth/10.0)
			{
				float sacrificedHealth = health - maxHealth/10.0f;
				
				if(findAndFillAltar(player.getEntityWorld(), player, (int)(sacrificedHealth * 100f * getModifier(amount))))
				{
					player.setHealth(maxHealth/10.0f);
					setPlayerIncense(player, 0);
					player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionSoulFray.id, soulFrayDuration));
										
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static float getModifier(float amount)
	{
		return 1 + amount*scalingOfSacrifice;
	}
	
	public static boolean findAndFillAltar(World world, EntityPlayer player, int amount)
    {
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        IBloodAltar altarEntity = getAltar(world, posX, posY, posZ);

        if (altarEntity == null)
        {
            return false;
        }

        altarEntity.sacrificialDaggerCall(amount, false);
        altarEntity.startCycle();
        
        return true;
    }

    public static IBloodAltar getAltar(World world, int x, int y, int z)
    {
        TileEntity tileEntity = null;

        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                for (int k = -2; k <= 1; k++)
                {
                    tileEntity = world.getTileEntity(i + x, k + y, j + z);
                    
                    if(tileEntity instanceof IBloodAltar)
                    {
                    	return (IBloodAltar)tileEntity;
                    }
                }
            }
        }

        return null;
    }
}
