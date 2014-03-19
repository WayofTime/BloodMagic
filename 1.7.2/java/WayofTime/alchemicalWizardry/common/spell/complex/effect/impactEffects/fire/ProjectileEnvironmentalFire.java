package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileUpdateEffect;

public class ProjectileEnvironmentalFire extends ProjectileUpdateEffect 
{
	public ProjectileEnvironmentalFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onUpdateEffect(Entity projectile) 
	{
		Vec3 posVec = SpellHelper.getEntityBlockVector(projectile);
		
		int horizRange = this.powerUpgrades+1;
		int vertRange = (int)(0.5*(this.powerUpgrades+1));
		
		int posX = (int)(posVec.xCoord);
		int posY = (int)(posVec.yCoord);
		int posZ = (int)(posVec.zCoord);
		
		World worldObj = projectile.worldObj;
					
		for(int i=-horizRange; i<=horizRange; i++)
		{
			for(int j=-vertRange; j<=vertRange; j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
				{
					if(!worldObj.isAirBlock(posX+i, posY+j, posZ+k))
					{
						SpellHelper.evaporateWaterBlock(worldObj, posX + i, posY + j, posZ + k);
					}
				}
			}
		}
	}
}
