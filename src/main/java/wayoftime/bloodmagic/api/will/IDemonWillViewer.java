package wayoftime.bloodmagic.api.will;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface for Items that allow players to see Will inside of chunks
 */
public interface IDemonWillViewer
{
	boolean canSeeDemonWillAura(World world, ItemStack stack, PlayerEntity player);

	int getDemonWillAuraResolution(World world, ItemStack stack, PlayerEntity player);
}