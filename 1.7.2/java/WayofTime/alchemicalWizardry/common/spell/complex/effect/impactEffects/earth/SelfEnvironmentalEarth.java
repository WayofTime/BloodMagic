package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfEnvironmentalEarth extends SelfSpellEffect
{
	public SelfEnvironmentalEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		int horizRadius = this.powerUpgrades + 1;
		int vertRadius = this.potencyUpgrades + 2;
		
		Vec3 blockVec = SpellHelper.getEntityBlockVector(player);
		
		int posX = (int)(blockVec.xCoord);
		int posY = (int)(blockVec.yCoord);
		int posZ = (int)(blockVec.zCoord);
		
		for(int i=-horizRadius; i<=horizRadius; i++)
		{
			for(int j=0; j<vertRadius; j++)
			{
				for(int k=-horizRadius; k<=horizRadius; k++)
				{
//					if(k==0&&i==0)
//					{
//						continue;
//					}
					
					BlockTeleposer.swapBlocks(world, world, posX + i, posY + j, posZ+k, posX+i, posY+j-vertRadius, posZ+k);
				}
			}
		}
	}
}
