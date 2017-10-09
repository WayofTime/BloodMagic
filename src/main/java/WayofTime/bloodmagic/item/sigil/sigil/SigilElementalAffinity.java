package WayofTime.bloodmagic.item.sigil.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilElementalAffinity implements ISigil.Toggle {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        player.fallDistance = 0;
        player.extinguish();
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 1, true, false));
        player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2, 0, true, false));
    }

    @Override
    public int getCost() {
        return 200;
    }
}
