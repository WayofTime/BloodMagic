package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.world.World;

public interface IEnchantmentGlyph 
{
	public int getSubtractedStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount);
	public int getEnchantability(World world, int x, int y, int z, int meta);
}
