package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IProjectileImpactEffect
{
    public void onEntityImpact(Entity mop, Entity projectile);

    public void onTileImpact(World world, MovingObjectPosition mop);
}
