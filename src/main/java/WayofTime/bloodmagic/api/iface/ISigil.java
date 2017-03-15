package WayofTime.bloodmagic.api.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Used for all {@link WayofTime.bloodmagic.api.impl.ItemSigil} <b>EXCEPT</b>
 * Sigils of Holdings.
 */
public interface ISigil
{
    boolean performArrayEffect(World world, BlockPos pos);

    boolean hasArrayEffect();

    interface Holding
    {
        @Nonnull
        ItemStack getHeldItem(ItemStack holdingStack, EntityPlayer player);
    }
}
