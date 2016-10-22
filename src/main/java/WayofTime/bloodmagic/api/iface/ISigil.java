package WayofTime.bloodmagic.api.iface;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Used for all {@link WayofTime.bloodmagic.api.impl.ItemSigil} <b>EXCEPT</b>
 * Sigils of Holdings.
 */
public interface ISigil
{
    boolean performArrayEffect(World world, BlockPos pos);

    boolean hasArrayEffect();
}
