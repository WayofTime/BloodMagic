package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.world.World;

public interface IEnchantmentGlyph extends IStabilityGlyph
{
	public int getEnchantability(World world, int x, int y, int z, int meta);
	public int getEnchantmentLevel(World world, int x, int y, int z, int meta);
}
