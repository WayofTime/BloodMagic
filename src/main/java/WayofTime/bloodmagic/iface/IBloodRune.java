package WayofTime.bloodmagic.iface;


import WayofTime.bloodmagic.block.enums.BloodRuneType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public interface IBloodRune {

    @Nullable
    BloodRuneType getBloodRune(IBlockAccess world, BlockPos pos, BlockState state);
}
