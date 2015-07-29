package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IEnchantmentGlyph extends IStabilityGlyph
{
	public int getEnchantability(World world, BlockPos pos, int meta);
	public int getEnchantmentLevel(World world, BlockPos pos, int meta);
}
