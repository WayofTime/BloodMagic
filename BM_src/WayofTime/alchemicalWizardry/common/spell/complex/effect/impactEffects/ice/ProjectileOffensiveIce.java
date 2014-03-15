package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileOffensiveIce extends ProjectileImpactEffect 
{
	public ProjectileOffensiveIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{
		if(mop instanceof EntityLivingBase)
		{
			((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,60*(this.powerUpgrades+1),this.potencyUpgrades));
		}
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		return;
	}
}
