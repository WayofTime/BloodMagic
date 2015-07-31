package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.world.World;

public interface IStabilityGlyph 
{
	public int getAdditionalStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount);
}
