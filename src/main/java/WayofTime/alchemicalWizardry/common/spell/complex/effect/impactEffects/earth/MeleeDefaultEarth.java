package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;

public class MeleeDefaultEarth extends MeleeSpellCenteredWorldEffect
{
    public MeleeDefaultEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(3 * power + 2);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, BlockPos pos)
    {
        int radius = this.potencyUpgrades;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    if (!world.isAirBlock(newPos) && world.getTileEntity(newPos) == null)
                    {
                    	IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();

                        if (block.getBlockHardness(world, newPos) == -1)
                        {
                            continue;
                        }

                        EntityFallingBlock entity = new EntityFallingBlock(world, newPos.getX() + 0.5f, newPos.getY() + 0.5f, newPos.getZ() + 0.5f, state);
                        world.spawnEntityInWorld(entity);
                    }
                }
            }
        }
    }
}
