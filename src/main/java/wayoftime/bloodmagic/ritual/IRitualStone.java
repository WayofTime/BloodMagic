package wayoftime.bloodmagic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRitualStone
{
	boolean isRuneType(Level world, BlockPos pos, EnumRuneType runeType);

	void setRuneType(Level world, BlockPos pos, EnumRuneType runeType);

}
