package wayoftime.bloodmagic.altar;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.block.enums.BloodRuneType;

/**
 * Any Block that implements this interface will be considered as Blood Runes
 * for the Blood Altar
 */
public interface IBloodRune
{
	@Nullable
	BloodRuneType getBloodRune(Level world, BlockPos pos);

	default int getRuneCount(Level world, BlockPos pos)
	{
		return 1;
	}
}
