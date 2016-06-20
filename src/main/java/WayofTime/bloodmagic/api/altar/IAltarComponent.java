package WayofTime.bloodmagic.api.altar;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAltarComponent
{
    EnumAltarComponent getType(World world, IBlockState state, BlockPos pos);
}
