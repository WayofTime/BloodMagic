package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SelfEnvironmentalIce extends SelfSpellEffect
{
    public SelfEnvironmentalIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onSelfUse(World world, EntityPlayer player)
    {
        EnumFacing look = SpellHelper.getCompassDirectionForLookVector(player.getLookVec());

        int width = this.potencyUpgrades + 1;
        int length = 5 * this.powerUpgrades + 3;

        int xOffset = look.getFrontOffsetX();
        int zOffset = look.getFrontOffsetZ();

        BlockPos pos = player.getPosition();

        for (int i = -width; i <= width; i++)
        {
            for (int j = 0; j < length; j++)
            {
            	BlockPos newPos = pos.add(i * (zOffset) + j * (xOffset), 0, i * (xOffset) + j * (zOffset));
                if (world.isAirBlock(newPos))
                {
                    world.setBlockState(newPos, Blocks.ice.getDefaultState());
                }
            }
        }
    }
}
