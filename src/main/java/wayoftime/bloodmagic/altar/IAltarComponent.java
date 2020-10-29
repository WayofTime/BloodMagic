package wayoftime.bloodmagic.altar;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAltarComponent
{
	@Nullable
	ComponentType getType(World world, BlockState state, BlockPos pos);
}