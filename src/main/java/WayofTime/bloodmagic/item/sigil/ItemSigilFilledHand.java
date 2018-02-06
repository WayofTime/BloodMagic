package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.apibutnotreally.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.handler.event.GenericHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilFilledHand extends ItemSigilToggleableBase {
    public ItemSigilFilledHand() {
        super("hand", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;
        GenericHandler.filledHandMap.put(player, 4);
    }
}
