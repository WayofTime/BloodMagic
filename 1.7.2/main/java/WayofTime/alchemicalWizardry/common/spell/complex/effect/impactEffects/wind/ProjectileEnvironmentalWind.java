package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileUpdateEffect;

public class ProjectileEnvironmentalWind extends ProjectileUpdateEffect 
{	
	public ProjectileEnvironmentalWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onUpdateEffect(Entity projectile) 
	{
		Vec3 posVec = SpellHelper.getEntityBlockVector(projectile);
		
		int horizRange = this.powerUpgrades+1;
		int vertRange = 1*this.potencyUpgrades+1;
		
		World worldObj = projectile.worldObj;
		
		if(projectile instanceof EntitySpellProjectile)
		{
			Entity shooter = ((EntitySpellProjectile) projectile).shootingEntity;
			if(shooter instanceof EntityPlayer)
			{
				List<Entity> entitylist = SpellHelper.getEntitiesInRange(worldObj, projectile.posX, projectile.posY, projectile.posZ, horizRange, vertRange);
				if(entitylist !=null)
				{
					for(Entity entity : entitylist)
					{
						if(entity instanceof EntityItem)
						{
							((EntityItem)entity).delayBeforeCanPickup = 0;
							entity.onCollideWithPlayer((EntityPlayer)shooter);
						}
					}
				}
			}
		}
		
		
	}
}
