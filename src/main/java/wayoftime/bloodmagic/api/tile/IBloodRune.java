package wayoftime.bloodmagic.api.tile;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any Block that implements this interface wil be considered as Blood Runes for the Blood Altar
 */
public interface IBloodRune
{
	@Nullable
	BloodRuneType getBloodRune(World world, BlockPos pos);
}
