package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeDefensiveEarth extends MeleeSpellCenteredWorldEffect
{
    public MeleeDefensiveEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(3 * power + 2);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, BlockPos pos)
    {
        EnumFacing dir = SpellHelper.getDirectionForLookVector(player.getLook(1));

        int vertRadius = (int) (2 + 1.0f / 2.0f * Math.pow(this.potencyUpgrades, 2) + 1.0f / 2.0f * this.potencyUpgrades);
        int horizRadius = this.potencyUpgrades + 1;

        int xOff = dir.getFrontOffsetX();
        int zOff = dir.getFrontOffsetZ();

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int j = 0; j < vertRadius; j++)
            {
                BlockTeleposer.swapBlocks(this, world, world, pos.add(i * zOff, j, i * xOff), pos.add(i * zOff, j - vertRadius, i * xOff));
            }
        }
    }
}
