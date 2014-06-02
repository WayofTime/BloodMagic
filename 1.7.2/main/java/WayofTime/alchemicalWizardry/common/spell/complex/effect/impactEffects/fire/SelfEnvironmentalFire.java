package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfEnvironmentalFire extends SelfSpellEffect
{
	public SelfEnvironmentalFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        
        int powRadius = this.powerUpgrades;
        int potRadius = this.potencyUpgrades-1;
        
        for(int i=-powRadius;i<=powRadius;i++)
        {
        	for(int j=-powRadius;j<=powRadius;j++)
        	{
        		for(int k=-powRadius;k<=powRadius;k++)
        		{
        			if(world.isAirBlock(posX+i, posY+j, posZ+k))
        			{
        				world.setBlock(posX+i, posY+j, posZ+k, Blocks.fire);
        			
        			}
        		}
        	}
        }
        
        for(int i=-potRadius;i<=potRadius;i++)
        {
        	for(int j=-potRadius;j<=potRadius;j++)
        	{
        		for(int k=-potRadius;k<=potRadius;k++)
        		{
        			if(!world.isAirBlock(posX+i, posY+j, posZ+k))
        			{
        				SpellHelper.smeltBlockInWorld(world, posX+i, posY+j, posZ+k);
        			}
        		}
        	}
        }
        
	}
}
