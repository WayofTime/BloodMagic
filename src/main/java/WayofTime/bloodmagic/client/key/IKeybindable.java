package WayofTime.bloodmagic.client.key;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IKeybindable {
    void onKeyPressed(ItemStack stack, EntityPlayer player, KeyBindings key, boolean showInChat);
}
