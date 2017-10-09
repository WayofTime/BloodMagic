package WayofTime.bloodmagic.item.sigil.sigil;

import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilFrost implements ISigil.Toggle {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        EnchantmentFrostWalker.freezeNearby(player, world, player.getPosition(), 1);
    }

    @Override
    public int getCost() {
        return 100;
    }
}
