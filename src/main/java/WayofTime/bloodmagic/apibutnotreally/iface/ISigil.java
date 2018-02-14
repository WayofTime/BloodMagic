package WayofTime.bloodmagic.apibutnotreally.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Used for all {@link WayofTime.bloodmagic.apibutnotreally.impl.ItemSigil} <b>EXCEPT</b>
 * Sigils of Holdings.
 */
public interface ISigil {

    default boolean performArrayEffect(World world, BlockPos pos) {
        return false;
    }

    default boolean hasArrayEffect() {
        return false;
    }

    interface Holding {
        @Nonnull
        ItemStack getHeldItem(ItemStack holdingStack, EntityPlayer player);
    }
}
