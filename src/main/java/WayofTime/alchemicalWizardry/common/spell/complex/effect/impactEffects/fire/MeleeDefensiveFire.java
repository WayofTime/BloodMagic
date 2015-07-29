package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeDefensiveFire extends MeleeSpellWorldEffect
{
    public MeleeDefensiveFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onWorldEffect(World world, EntityPlayer entityPlayer)
    {
        EnumFacing look = SpellHelper.getCompassDirectionForLookVector(entityPlayer.getLookVec());

        int width = this.potencyUpgrades + 1;
        int length = 5 * this.powerUpgrades + 3;

        int xOffset = look.getFrontOffsetX();
        int zOffset = look.getFrontOffsetZ();

        BlockPos pos = entityPlayer.getPosition().add(look.getDirectionVec());

        for (int i = -width; i <= width; i++)
        {
            for (int j = 0; j < length; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                	BlockPos newPos = pos.add(i * (zOffset) + j * (xOffset), k, i * (xOffset) + j * (zOffset));
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.fire.getDefaultState());
                    }
                }
            }
        }
    }
}
