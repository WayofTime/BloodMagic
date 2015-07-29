package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import WayofTime.alchemicalWizardry.api.spell.ProjectileUpdateEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class ProjectileEnvironmentalIce extends ProjectileUpdateEffect
{

    public ProjectileEnvironmentalIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onUpdateEffect(Entity projectile)
    {
    	BlockPos pos = projectile.getPosition();
    	
        int horizRange = this.powerUpgrades + 1;
        int vertRange = this.potencyUpgrades + 1;

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                    SpellHelper.freezeWaterBlock(projectile.worldObj, pos.add(i, j, k));
                }
            }
        }
    }
}
