package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileDefaultFire extends ProjectileImpactEffect
{
	public ProjectileDefaultFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop) 
	{
		mop.setFire((int)Math.pow(2,this.powerUpgrades));
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		int x = mop.blockX;
		int y = mop.blockY;
		int z = mop.blockZ;
		int range = 0;
		for(int i=-range; i<=range;i++)
		{
			for(int j=-range; j<=range;j++)
			{
				for(int k=-range; k<=range; k++)
				{
					if(world.isAirBlock(x+i, y+j, z+k))
					{
						world.setBlock(x+i, y+j, z+k, Blocks.fire);
					}
				}
			}
		}
		
	}
}
