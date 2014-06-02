package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfOffensiveEarth extends SelfSpellEffect
{

	public SelfOffensiveEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		int horizRadius = this.powerUpgrades;
		int vertRadius = this.potencyUpgrades + 1;
		
		Vec3 blockVec = SpellHelper.getEntityBlockVector(player);
		
		int posX = (int)(blockVec.xCoord);
		int posY = (int)(blockVec.yCoord);
		int posZ = (int)(blockVec.zCoord);
		
		for(int i=-horizRadius; i<=horizRadius; i++)
		{
			for(int j=-vertRadius; j<0; j++)
			{
				for(int k=-horizRadius; k<=horizRadius; k++)
				{
					if(world.rand.nextFloat()<0.7f)
					{
						SpellHelper.smashBlock(world, posX+i, posY+j, posZ+k);
					}
				}
			}
		}
	}
}
