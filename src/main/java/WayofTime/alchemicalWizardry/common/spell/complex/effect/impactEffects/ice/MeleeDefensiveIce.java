package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeDefensiveIce extends MeleeSpellWorldEffect
{
    public MeleeDefensiveIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onWorldEffect(World world, EntityPlayer entityPlayer)
    {
        EnumFacing look = SpellHelper.getCompassDirectionForLookVector(entityPlayer.getLookVec());

        int width = this.powerUpgrades;
        int height = this.powerUpgrades + 2;

        int xOffset = look.getFrontOffsetX();
        int zOffset = look.getFrontOffsetZ();

        int range = this.potencyUpgrades + 1;

        BlockPos pos = entityPlayer.getPosition();

        int xStart = pos.getX() + range * xOffset;
        int zStart = pos.getZ() + range * zOffset;
        int yStart = pos.getY();

        for (int i = -width; i <= width; i++)
        {
            for (int j = 0; j < height; j++)
            {
            	BlockPos newPos = new BlockPos(xStart + i * (zOffset), yStart + j, zStart + i * (xOffset));
                if (world.isAirBlock(newPos))
                {
                    world.setBlockState(newPos, Blocks.ice.getDefaultState());
                }
            }
        }
    }
}
