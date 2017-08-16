package WayofTime.bloodmagic.api.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISentientTool {
    boolean spawnSentientEntityOnDrop(ItemStack droppedStack, EntityPlayer player);
}
