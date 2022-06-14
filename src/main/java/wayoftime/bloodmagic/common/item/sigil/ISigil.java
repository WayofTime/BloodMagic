package wayoftime.bloodmagic.common.item.sigil;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Used for all ItemSigils <b>EXCEPT</b> for Sigils of Holding.
 */
public interface ISigil
{
	default boolean performArrayEffect(Level world, BlockPos pos)
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
		ItemStack getHeldItem(ItemStack holdingStack, Player player);
	}
}