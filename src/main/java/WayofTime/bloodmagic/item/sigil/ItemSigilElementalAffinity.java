package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilElementalAffinity extends ItemSigilToggleableBase {
    public ItemSigilElementalAffinity() {
        super("elemental_affinity", 200);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.fallDistance = 0;
        player.extinguish();
        player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2, 1, true, false));
        player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 2, 0, true, false));
    }
}
