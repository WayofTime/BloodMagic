package wayoftime.bloodmagic.client.key;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IKeybindable
{
	void onKeyPressed(ItemStack stack, PlayerEntity player, KeyBindings key, boolean showInChat);
}
