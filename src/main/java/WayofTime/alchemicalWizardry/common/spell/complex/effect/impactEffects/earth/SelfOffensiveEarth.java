package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SelfOffensiveEarth extends SelfSpellEffect
{

    public SelfOffensiveEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        int horizRadius = this.powerUpgrades;
        int vertRadius = this.potencyUpgrades + 1;

        BlockPos pos = player.getPosition();

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int j = -vertRadius; j < 0; j++)
            {
                for (int k = -horizRadius; k <= horizRadius; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    if (world.rand.nextFloat() < 0.7f)
                    {
                        SpellHelper.smashBlock(world, newPos);
                    }
                }
            }
        }
    }
}
