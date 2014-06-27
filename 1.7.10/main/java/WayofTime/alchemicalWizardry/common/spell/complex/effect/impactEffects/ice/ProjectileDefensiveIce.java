package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileDefensiveIce extends ProjectileImpactEffect 
{
	public ProjectileDefensiveIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{
		return;
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		int horizRadius = this.powerUpgrades+1;
		int vertRadius = this.potencyUpgrades;
		
		int posX = mop.blockX;
		int posY = mop.blockY;
		int posZ = mop.blockZ;
		
		for(int i=-horizRadius; i<=horizRadius; i++)
		{
			for(int k=-horizRadius; k<=horizRadius; k++)
			{
				for(int j=-vertRadius; j<=vertRadius; j++)
				{
					SpellHelper.freezeWaterBlock(world, posX+i, posY+j, posZ+k);
					
					if(world.isAirBlock(posX+i, posY+j, posZ+k)&&!world.isAirBlock(posX+i, posY+j-1, posZ+k))
					{
						world.setBlock(posX+i, posY+j, posZ+k, Blocks.snow);
					}
				}
			}
		}
	}
}
