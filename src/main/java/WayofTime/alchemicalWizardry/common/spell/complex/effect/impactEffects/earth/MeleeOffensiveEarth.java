package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MeleeOffensiveEarth extends MeleeSpellCenteredWorldEffect
{
    public MeleeOffensiveEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(3 * power + 2);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ)
    {
        int radius = this.potencyUpgrades;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    SpellHelper.smashBlock(world, posX + i, posY + j, posZ + k);
                }
            }
        }
    }
}
