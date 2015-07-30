package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IEnchantmentGlyph extends IStabilityGlyph
{
	int getEnchantability(World world, BlockPos pos, int meta);

	int getEnchantmentLevel(World world, BlockPos pos, int meta);
}
