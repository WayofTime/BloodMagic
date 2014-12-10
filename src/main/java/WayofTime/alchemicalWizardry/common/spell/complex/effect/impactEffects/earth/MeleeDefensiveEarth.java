package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MeleeDefensiveEarth extends MeleeSpellCenteredWorldEffect
{
    public MeleeDefensiveEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(3 * power + 2);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ)
    {
        ForgeDirection dir = SpellHelper.getDirectionForLookVector(player.getLook(1));

        int vertRadius = (int) (2 + 1.0f / 2.0f * Math.pow(this.potencyUpgrades, 2) + 1.0f / 2.0f * this.potencyUpgrades);
        int horizRadius = this.potencyUpgrades + 1;

        int xOff = dir.offsetX;
        int zOff = dir.offsetZ;

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int j = 0; j < vertRadius; j++)
            {
                BlockTeleposer.swapBlocks(this, world, world, posX + i * zOff, posY + j, posZ + i * xOff, posX + i * zOff, posY + j - vertRadius, posZ + i * xOff);
            }
        }
    }
}
