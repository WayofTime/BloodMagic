package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IStabilityGlyph 
{
	public int getAdditionalStabilityForFaceCount(World world, BlockPos pos, int meta, int faceCount);
}
