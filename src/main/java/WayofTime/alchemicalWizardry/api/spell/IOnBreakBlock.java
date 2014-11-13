package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IOnBreakBlock
{
    public abstract int onBlockBroken(ItemStack container, World world, EntityPlayer player, Block block, int meta, int x, int y, int z, ForgeDirection sideBroken);
}
