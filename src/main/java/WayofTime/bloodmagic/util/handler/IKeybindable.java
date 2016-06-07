package WayofTime.bloodmagic.util.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IKeybindable
{
    void onKeyPressed(ItemStack stack, EntityPlayer player, BMKeyBinding.Key key, boolean showInChat);
}
