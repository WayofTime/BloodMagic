package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SelfDefaultIce extends SelfSpellEffect
{
    public SelfDefaultIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        BlockPos pos = player.getPosition();

        double yVel = 1 * (0.4 * this.powerUpgrades + 0.75);
        SpellHelper.setPlayerSpeedFromServer(player, player.motionX, yVel, player.motionZ);

        for (int i = 0; i < 2; i++)
        {
        	BlockPos newPos = pos.add(0, i, 0);
            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, Blocks.ice.getDefaultState());
            }
        }

        player.fallDistance = 0.0f;
    }
}
