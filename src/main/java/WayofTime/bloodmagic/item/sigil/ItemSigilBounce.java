package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class ItemSigilBounce extends ItemSigilToggleableBase {
    public ItemSigilBounce() {
        super("bounce", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.addPotionEffect(new EffectInstance(RegistrarBloodMagic.BOUNCE, 2, 0, true, false));
    }
}
