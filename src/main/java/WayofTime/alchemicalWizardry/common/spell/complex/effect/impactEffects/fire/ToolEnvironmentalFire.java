package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.OnBreakBlockEffect;

public class ToolEnvironmentalFire extends OnBreakBlockEffect
{
    public ToolEnvironmentalFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int onBlockBroken(ItemStack container, World world, EntityPlayer player, Block block, IBlockState state, BlockPos pos, EnumFacing sideBroken) 
    {
        int amount = 0;
        int cost = (int) (250 * (1 - 0.1f * powerUpgrades) * Math.pow(0.85, costUpgrades));
        int radius = this.powerUpgrades;
        float chance = 0.35f + 0.15f * this.potencyUpgrades;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                	BlockPos newPos = pos.add(i - sideBroken.getFrontOffsetX(), j, k - sideBroken.getFrontOffsetZ());
                	IBlockState newState = world.getBlockState(newPos);
                    Block blockAffected = newState.getBlock();

                    if ((new Random().nextFloat() <= chance) && (blockAffected == Blocks.gravel || blockAffected == Blocks.stone || blockAffected == Blocks.cobblestone))
                    {
                        world.setBlockState(newPos, Blocks.lava.getDefaultState());
                        amount += cost;
                    }
                }
            }
        }

        return amount;
    }
}
