package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ProjectileDefensiveIce extends ProjectileImpactEffect
{
    public ProjectileDefensiveIce(int power, int potency, int cost)
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
        
        BlockPos pos = mop.func_178782_a();

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int k = -horizRadius; k <= horizRadius; k++)
            {
                for (int j = -vertRadius; j <= vertRadius; j++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    SpellHelper.freezeWaterBlock(world, newPos);

                    if (world.isAirBlock(newPos) && !world.isAirBlock(newPos.offsetDown()))
                    {
                        world.setBlockState(newPos, Blocks.snow.getDefaultState());
                    }
                }
            }
        }
    }
}
