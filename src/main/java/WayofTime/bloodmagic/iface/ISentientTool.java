package WayofTime.bloodmagic.iface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ISentientTool {
    boolean spawnSentientEntityOnDrop(ItemStack droppedStack, PlayerEntity player);
}
