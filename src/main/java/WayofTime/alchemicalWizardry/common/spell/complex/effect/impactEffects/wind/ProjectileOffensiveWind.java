package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;

public class ProjectileOffensiveWind extends ProjectileImpactEffect
{

    public ProjectileOffensiveWind(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        if (mop instanceof EntityLivingBase)
        {
            ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionHeavyHeart.id, (int) (100 * (2 * this.powerUpgrades + 1) * (1 / (this.potencyUpgrades + 1))), this.potencyUpgrades));
        }
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
        return;
    }
}
