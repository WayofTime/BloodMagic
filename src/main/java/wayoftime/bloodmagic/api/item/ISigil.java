package wayoftime.bloodmagic.api.item;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.item.ItemSigil;

/**
 * Used for all {@link ItemSigil} <b>EXCEPT</b> Sigils of Holdings.
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