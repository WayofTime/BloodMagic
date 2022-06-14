package wayoftime.bloodmagic.api.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Interface for Items that allow players to see Will inside of chunks
 */
public interface IDemonWillViewer
{
	boolean canSeeDemonWillAura(Level world, ItemStack stack, Player player);

	int getDemonWillAuraResolution(Level world, ItemStack stack, Player player);
}