package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileUpdateEffect;

public class ProjectileEnvironmentalIce extends ProjectileUpdateEffect 
{

	public ProjectileEnvironmentalIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onUpdateEffect(Entity projectile) 
	{
		Vec3 posVec = SpellHelper.getEntityBlockVector(projectile);
		
		int horizRange = this.powerUpgrades+1;
		int vertRange = this.potencyUpgrades+1;
		
		int posX = (int)(posVec.xCoord);
		int posY = (int)(posVec.yCoord);
		int posZ = (int)(posVec.zCoord);
		
		for(int i=-horizRange; i<=horizRange; i++)
		{
			for(int j=-vertRange; j<=vertRange; j++)
			{
				for(int k=-horizRange; k<=horizRange; k++)
				{
					SpellHelper.freezeWaterBlock(projectile.worldObj, posX+i, posY+j, posZ+k);
				}
			}
		}
	}
}
