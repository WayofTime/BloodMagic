package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileUpdateEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class ProjectileEnvironmentalFire extends ProjectileUpdateEffect
{
    public ProjectileEnvironmentalFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onUpdateEffect(Entity projectile)
    {
        BlockPos pos = projectile.getPosition();

        int horizRange = this.powerUpgrades + 1;
        int vertRange = (int) (0.5 * (this.powerUpgrades + 1));

        World worldObj = projectile.worldObj;

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (!worldObj.isAirBlock(newPos))
                    {
                        SpellHelper.evaporateWaterBlock(worldObj, newPos);
                    }
                }
            }
        }
    }
}
