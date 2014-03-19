package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IProjectileImpactEffect 
{
	public void onEntityImpact(Entity mop, Entity proj);
	public void onTileImpact(World world, MovingObjectPosition mop);
}
