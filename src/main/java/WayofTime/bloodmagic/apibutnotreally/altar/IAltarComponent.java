package WayofTime.bloodmagic.apibutnotreally.altar;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IAltarComponent {
    @Nullable
    EnumAltarComponent getType(World world, IBlockState state, BlockPos pos);
}
