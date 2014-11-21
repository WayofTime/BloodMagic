package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.world.World;

public interface IRitualStone
{
	public boolean isRuneType(World world, int x, int y, int z, int meta, int runeType);
}
