package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ITeleposerFocus extends IBindable
{
	public AABB getEntityRangeOffset(Level world, BlockPos teleposerPos);

	public List<BlockPos> getBlockListOffset(Level world);

	public BlockPos getStoredPos(ItemStack stack);

	public Level getStoredWorld(ItemStack stack, Level world);
}
