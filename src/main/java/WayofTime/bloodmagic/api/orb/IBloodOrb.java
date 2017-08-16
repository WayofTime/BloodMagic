package WayofTime.bloodmagic.api.orb;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IBloodOrb {
    @Nullable
    BloodOrb getOrb(ItemStack stack);
}
