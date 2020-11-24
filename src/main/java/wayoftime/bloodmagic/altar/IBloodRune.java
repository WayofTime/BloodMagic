package wayoftime.bloodmagic.altar;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.block.enums.BloodRuneType;

/**
 * Any Block that implements this interface wil be considered as Blood Runes for the Blood Altar
 */
public interface IBloodRune
{
	@Nullable
    BloodRuneType getBloodRune(World world, BlockPos pos);
}
