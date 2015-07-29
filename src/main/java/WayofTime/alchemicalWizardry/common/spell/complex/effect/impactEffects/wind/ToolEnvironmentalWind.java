package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.OnBreakBlockEffect;

public class ToolEnvironmentalWind extends OnBreakBlockEffect
{
    public ToolEnvironmentalWind(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int onBlockBroken(ItemStack container, World world, EntityPlayer player, Block block, IBlockState state, BlockPos pos, EnumFacing sideBroken)
    {
        double vertRange = 0.5 + (this.powerUpgrades * this.powerUpgrades + this.powerUpgrades) / 2;
        double horizRange = vertRange;

        List<EntityItem> itemList = SpellHelper.getItemsInRange(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, horizRange, vertRange);

        for (EntityItem entity : itemList)
        {
            if (!world.isRemote)
            {
                entity.setPickupDelay(0);
                entity.onCollideWithPlayer(player);
            }
        }

        return 0;
    }

}
