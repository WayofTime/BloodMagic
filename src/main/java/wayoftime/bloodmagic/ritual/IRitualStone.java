package wayoftime.bloodmagic.ritual;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRitualStone
{
	boolean isRuneType(World world, BlockPos pos, EnumRuneType runeType);

	void setRuneType(World world, BlockPos pos, EnumRuneType runeType);

	interface Tile
	{
		boolean isRuneType(EnumRuneType runeType);

		EnumRuneType getRuneType();

		void setRuneType(EnumRuneType runeType);
	}
}
