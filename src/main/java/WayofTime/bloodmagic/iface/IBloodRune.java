package wayoftime.bloodmagic.iface;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.block.enums.BloodRuneType;

public interface IBloodRune
{

	@Nullable
	BloodRuneType getBloodRune(World world, BlockPos pos);
}
