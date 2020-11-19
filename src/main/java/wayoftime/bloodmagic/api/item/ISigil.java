package wayoftime.bloodmagic.api.item;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Used for all ItemSigils <b>EXCEPT</b> for Sigils of Holding.
 */
public interface ISigil
{
	default boolean performArrayEffect(World world, BlockPos pos)
	{
		return false;
	}

	default boolean hasArrayEffect()
	{
		return false;
	}

	interface Holding
	{
		@Nonnull
		ItemStack getHeldItem(ItemStack holdingStack, PlayerEntity player);
	}
}