package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class ProjectileDefensiveFire extends ProjectileImpactEffect
{
    public ProjectileDefensiveFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        mop.setFire(3 * (this.potencyUpgrades + 1));
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
        int horizRange = (int) ((this.powerUpgrades));
        int vertRange = (int) ((this.powerUpgrades));

        BlockPos pos = mop.func_178782_a();

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (!world.isAirBlock(newPos))
                    {
                        SpellHelper.smeltBlockInWorld(world, newPos);
                    }
                }
            }
        }
    }
}
