package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ProjectileDefaultWind extends ProjectileImpactEffect
{
    public ProjectileDefaultWind(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        mop.motionX = proj.motionX;
        mop.motionZ = proj.motionZ;
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {

    }
}
