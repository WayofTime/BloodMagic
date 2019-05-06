package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.compress.CompressionHandler;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilCompression extends ItemSigilToggleableBase {
    public ItemSigilCompression() {
        super("compression", 200);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {

        if (PlayerHelper.isFakePlayer(player) || world.getTotalWorldTime() % 100 != 0)
            return;

        CompressionHandler.compressInventory(player.inventory, world);
    }
}
