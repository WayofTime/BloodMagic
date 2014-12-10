package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import WayofTime.alchemicalWizardry.api.spell.SelfSpellEffect;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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

        Vec3 blockVec = SpellHelper.getEntityBlockVector(player);

        int posX = (int) (blockVec.xCoord);
        int posY = (int) (blockVec.yCoord) - 1;
        int posZ = (int) (blockVec.zCoord);

        for (int i = -horizRadius; i <= horizRadius; i++)
        {
            for (int k = -horizRadius; k <= horizRadius; k++)
            {
                if (!world.isAirBlock(posX + i, posY, posZ + k))
                {
                    continue;
                }

                for (int j = -1; j >= -vertRange; j--)
                {
                    if (!world.isAirBlock(posX + i, posY + j, posZ + k) && !SpellHelper.isBlockFluid(world.getBlock(posX + i, posY + j, posZ + k)))
                    {
                        BlockTeleposer.swapBlocks(this, world, world, posX + i, posY, posZ + k, posX + i, posY + j, posZ + k);

                        break;
                    }
                }
            }
        }
    }
}
