package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SelfOffensiveFire extends SelfSpellEffect
{
    public SelfOffensiveFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFlameCloak.id, 300 * (2 * this.powerUpgrades + 1), this.potencyUpgrades));
    }
}
