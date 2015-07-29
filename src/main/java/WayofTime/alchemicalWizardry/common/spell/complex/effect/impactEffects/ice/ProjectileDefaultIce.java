package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;

public class ProjectileDefaultIce extends ProjectileImpactEffect
{
    public ProjectileDefaultIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        return;
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
        int horizRadius = this.powerUpgrades + 1;
        int vertRadius = this.potencyUpgrades;

        EnumFacing sideHit = mop.field_178784_b;

        BlockPos newPos = mop.func_178782_a().add(sideHit.getDirectionVec());
        if (world.isAirBlock(newPos))
        {
            world.setBlockState(newPos, Blocks.ice.getDefaultState());
        }
    }
}
