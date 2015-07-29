package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ExtrapolatedMeleeEntityEffect;

public class MeleeOffensiveIce extends ExtrapolatedMeleeEntityEffect
{
    public MeleeOffensiveIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setMaxNumberHit(1 + potency);
        this.setRadius(2);
        this.setRange(3);
    }

    @Override
    protected boolean entityEffect(World world, Entity entity, EntityPlayer entityPlayer)
    {
    	BlockPos pos = entityPlayer.getPosition();


        double yVel = 1 * (0.3 * this.powerUpgrades + 0.90);

        entity.motionY = yVel;

        for (int i = 0; i < 2; i++)
        {
        	BlockPos newPos = pos.offsetUp(i);
            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, Blocks.ice.getDefaultState());
            }
        }

        entity.fallDistance = 0.0f;
        return true;
    }

}
