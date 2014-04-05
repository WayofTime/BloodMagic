package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileOffensiveFire extends ProjectileImpactEffect 
{
	public ProjectileOffensiveFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{
		int horizRange = (int)(this.powerUpgrades);
		int vertDepth = (int)(3*this.potencyUpgrades+1);
		
		Vec3 blockVector = SpellHelper.getEntityBlockVector(mop);
		
		int posX = (int)(blockVector.xCoord);
		int posY = (int)(blockVector.yCoord);
		int posZ = (int)(blockVector.zCoord);
		
		World world = mop.worldObj;
		
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

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{		
//		int horizRange = (int)(this.powerUpgrades);
//		int vertRange = (int)(this.potencyUpgrades);
//		
//		int posX = mop.blockX;
//		int posY = mop.blockY;
//		int posZ = mop.blockZ;
//		
//		for(int i=-horizRange; i<=horizRange; i++)
//		{
//			for(int j=-vertRange; j<=vertRange; j++)
//			{
//				for(int k=-horizRange; k<=horizRange; k++)
//				{
//					if(!world.isAirBlock(posX+i, posY+j, posZ+k))
//					{
//						Block block = world.getBlock(posX+i, posY+j, posZ+k);
//						if(block == null || block.getBlockHardness(world, posX+i, posY+j, posZ+k)==-1)
//						{
//							continue;
//						}
//						//block.breakBlock(world, posX+i, posY+j, posZ+k, block.blockID, world.getBlockMetadata(posX+i, posY+j, posZ+k));
//						//world.destroyBlock(posX+i, posY+j, posZ+k, true);
//						if(world.rand.nextFloat()<0.6f)
//						{
//							SpellHelper.smashBlock(world, posX+i, posY+j, posZ+k);
//						}
//					}
//				}
//			}
//		}
	}
}
