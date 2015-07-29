package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeEnvironmentalFire extends MeleeSpellCenteredWorldEffect
{
    public MeleeEnvironmentalFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(3 * power + 2);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, BlockPos pos)
    {
        int radius = this.potencyUpgrades;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    SpellHelper.evaporateWaterBlock(world, newPos);
                }
            }
        }
    }
}
