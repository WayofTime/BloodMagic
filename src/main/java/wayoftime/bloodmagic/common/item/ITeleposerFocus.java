package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITeleposerFocus extends IBindable
{
	public AxisAlignedBB getEntityRangeOffset(World world, BlockPos teleposerPos);

	public List<BlockPos> getBlockListOffset(World world);

	public BlockPos getStoredPos(ItemStack stack);

	public World getStoredWorld(ItemStack stack, World world);
}
