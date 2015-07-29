package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SelfDefaultEarth extends SelfSpellEffect
{

    public SelfDefaultEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        int horizRadius = this.powerUpgrades;
        int vertRange = 5 + 10 * this.potencyUpgrades;

        BlockPos pos = player.getPosition().offsetDown();

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int k = -horizRadius; k <= horizRadius; k++)
            {
            	BlockPos newPos = pos.add(i, 0, k);
                if (!world.isAirBlock(newPos))
                {
                    continue;
                }

                for (int j = -1; j >= -vertRange; j--)
                {
                	BlockPos newNewPos = newPos.add(0, j, 0);
                    if (!world.isAirBlock(newNewPos) && !SpellHelper.isBlockFluid(world.getBlockState(newNewPos).getBlock()))
                    {
                        BlockTeleposer.swapBlocks(this, world, world,newPos, newNewPos);

                        break;
                    }
                }
            }
        }
    }
}
