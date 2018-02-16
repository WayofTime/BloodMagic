package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilFrost extends ItemSigilToggleableBase {
    public ItemSigilFrost() {
        super("frost", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        EnchantmentFrostWalker.freezeNearby(player, world, player.getPosition(), 1);
    }
}
