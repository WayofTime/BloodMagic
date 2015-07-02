package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IProjectileImpactEffect
{
    void onEntityImpact(Entity mop, Entity projectile);

    void onTileImpact(World world, MovingObjectPosition mop);
}
