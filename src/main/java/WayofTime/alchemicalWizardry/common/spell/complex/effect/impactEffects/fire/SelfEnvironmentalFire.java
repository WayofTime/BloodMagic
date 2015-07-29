package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SelfEnvironmentalFire extends SelfSpellEffect
{
    public SelfEnvironmentalFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        BlockPos pos = player.getPosition();

        int powRadius = this.powerUpgrades;
        int potRadius = this.potencyUpgrades - 1;

        for (int i = -powRadius; i <= powRadius; i++)
        {
            for (int j = -powRadius; j <= powRadius; j++)
            {
                for (int k = -powRadius; k <= powRadius; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.fire.getDefaultState());

                    }
                }
            }
        }

        for (int i = -potRadius; i <= potRadius; i++)
        {
            for (int j = -potRadius; j <= potRadius; j++)
            {
                for (int k = -potRadius; k <= potRadius; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);

                    if (!world.isAirBlock(newPos))
                    {
                        SpellHelper.smeltBlockInWorld(world, newPos);
                    }
                }
            }
        }

    }
}
