package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;

public class SelfDefensiveFire extends SelfSpellEffect
{

    public SelfDefensiveFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        int horizRange = (int) (this.powerUpgrades);
        int vertDepth = (int) (3 * this.potencyUpgrades + 1);

        BlockPos pos = player.getPosition();

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertDepth; j < 0; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.flowing_lava.getStateFromMeta(7));
                    }
                }
            }
        }
    }
}
