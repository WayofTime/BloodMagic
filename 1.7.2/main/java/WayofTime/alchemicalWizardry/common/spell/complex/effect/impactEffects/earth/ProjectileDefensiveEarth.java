package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileUpdateEffect;

public class ProjectileDefensiveEarth extends ProjectileImpactEffect 
{
	public ProjectileDefensiveEarth(int power, int potency, int cost) 
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
		int horizRange = (int)(this.powerUpgrades);
		int vertRange = (int)(this.potencyUpgrades);
		
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
						if(block == null || block.getBlockHardness(world, posX+i, posY+j, posZ+k)==-1)
						{
							continue;
						}
						//block.breakBlock(world, posX+i, posY+j, posZ+k, block.blockID, world.getBlockMetadata(posX+i, posY+j, posZ+k));
						//world.destroyBlock(posX+i, posY+j, posZ+k, true);
						if(world.rand.nextFloat()<0.6f)
						{
							SpellHelper.smashBlock(world, posX+i, posY+j, posZ+k);
						}
					}
				}
			}
		}

	}

}
