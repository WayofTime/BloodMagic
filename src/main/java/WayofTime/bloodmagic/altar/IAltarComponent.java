package WayofTime.bloodmagic.altar;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IAltarComponent {
    @Nullable
    ComponentType getType(World world, BlockState state, BlockPos pos);
}
