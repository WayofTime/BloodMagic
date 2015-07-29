package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeEnvironmentalEarth extends MeleeSpellCenteredWorldEffect
{
    public MeleeEnvironmentalEarth(int power, int potency, int cost)
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
                        ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));

                        ItemStack dustStack = SpellHelper.getDustForOre(stack);

                        if (dustStack != null)
                        {
                            dustStack.stackSize = 3;
                            world.spawnEntityInWorld(new EntityItem(world, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, dustStack));

                            world.setBlockToAir(newPos);
                        }
                    }
                }
            }
        }
    }
}
