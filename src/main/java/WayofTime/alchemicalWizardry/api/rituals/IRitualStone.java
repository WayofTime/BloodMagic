package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRitualStone
{
	/**
	 * x, y, and z give the position of the Ritual Stone
	 * @param world
	 * @param pos
	 * @param state
	 * @param runeType
	 * @return
	 */
	boolean isRuneType(World world, BlockPos pos, IBlockState state, int runeType);
}
