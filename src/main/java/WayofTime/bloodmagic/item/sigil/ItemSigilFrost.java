package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilFrost extends ItemSigilToggleableBase {
    public ItemSigilFrost() {
        super("frost", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        FrostWalkerEnchantment.freezeNearby(player, world, player.getPosition(), 1);
    }
}
