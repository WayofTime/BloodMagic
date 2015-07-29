package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IOnBreakBlock
{
    int onBlockBroken(ItemStack container, World world, EntityPlayer player, Block block, IBlockState state, BlockPos pos, EnumFacing sideBroken);
}
