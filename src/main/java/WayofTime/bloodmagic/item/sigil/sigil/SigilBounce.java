package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilBounce implements ISigil.Toggle {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.BOUNCE, 2, 0, true, false));
    }

    @Override
    public int getCost() {
        return 100;
    }
}
