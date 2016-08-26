package WayofTime.bloodmagic.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IKeybindable
{
    void onKeyPressed(ItemStack stack, EntityPlayer player, KeyBindingBloodMagic.KeyBindings key, boolean showInChat);
}
