package wayoftime.bloodmagic.client.key;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IKeybindable
{
	void onKeyPressed(ItemStack stack, Player player, KeyBindings key, boolean showInChat);
}
