package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.world.World;

public interface IRitualStone
{
	/**
	 * x, y, and z give the position of the Ritual Stone
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param meta
	 * @param runeType
	 * @return
	 */
	public boolean isRuneType(World world, int x, int y, int z, int meta, int runeType);
}
