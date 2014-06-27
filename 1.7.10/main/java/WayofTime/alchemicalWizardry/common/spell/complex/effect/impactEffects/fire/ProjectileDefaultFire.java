package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileDefaultFire extends ProjectileImpactEffect
{
	public ProjectileDefaultFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{		
		Vec3 blockVec = SpellHelper.getEntityBlockVector(mop);
		
		int x = (int)(blockVec.xCoord);
		int y = (int)(blockVec.yCoord);
		int z = (int)(blockVec.zCoord);
		World world = mop.worldObj;
		
		int horizRange = 0;
		int vertRange = 0;
		
		for(int i=-horizRange; i<=horizRange;i++)
		{
			for(int j=-vertRange; j<=vertRange;j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
				{
					if(world.isAirBlock(x+i, y+j, z+k))
					{
						world.setBlock(x+i, y+j, z+k, Blocks.fire);
					}
				}
			}
		}
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		int x = mop.blockX;
		int y = mop.blockY;
		int z = mop.blockZ;
		
		int horizRange = 0;
		int vertRange = 0;
		
		for(int i=-horizRange; i<=horizRange;i++)
		{
			for(int j=-vertRange; j<=vertRange;j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
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
