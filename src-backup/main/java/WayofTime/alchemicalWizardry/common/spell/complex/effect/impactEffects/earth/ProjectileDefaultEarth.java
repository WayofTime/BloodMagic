package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileDefaultEarth extends ProjectileImpactEffect 
{
	public ProjectileDefaultEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{		
		int horizRange = (int)(0.5*(this.powerUpgrades)+1);
		int vertRange = (int)(0.5*(this.powerUpgrades)+1);
		
		int posX = mop.blockX;
		int posY = mop.blockY;
		int posZ = mop.blockZ;
		
		for(int i=-horizRange; i<=horizRange; i++)
		{
			for(int j=-vertRange; j<=vertRange; j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
				{
					if(!world.isAirBlock(posX+i, posY+j, posZ+k))
					{
						Block block = world.getBlock(posX+i, posY+j, posZ+k);
						if(block == null || block.getBlockHardness(world, posX+i, posY+j, posZ+k)==-1 || SpellHelper.isBlockFluid(block))
						{
							continue;
						}
						//block.breakBlock(world, posX+i, posY+j, posZ+k, block.blockID, world.getBlockMetadata(posX+i, posY+j, posZ+k));
						//world.destroyBlock(posX+i, posY+j, posZ+k, true);
						world.func_147480_a(posX+i, posY+j, posZ+k, false);
					}
				}
			}
		}

	}

}
