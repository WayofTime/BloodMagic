package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfDefensiveFire extends SelfSpellEffect {

	public SelfDefensiveFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		int horizRange = (int)(this.powerUpgrades);
		int vertDepth = (int)(3*this.potencyUpgrades+1);
		
		Vec3 blockVector = SpellHelper.getEntityBlockVector(player);
		
		int posX = (int)(blockVector.xCoord);
		int posY = (int)(blockVector.yCoord);
		int posZ = (int)(blockVector.zCoord);

		for(int i=-horizRange; i<=horizRange; i++)
		{
			for(int j=-vertDepth; j<0; j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
				{
					if(world.isAirBlock(posX+i, posY+j, posZ+k))
					{
						world.setBlock(posX + i, posY + j,  posZ + k, Blocks.flowing_lava,7,3);
					}
				}
			}
		}
	}
}
