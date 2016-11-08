package WayofTime.bloodmagic.api.iface;

import net.minecraft.item.ItemStack;

public interface IPurificationAsh
{
    double getTotalPurity(ItemStack stack);

    double getMaxPurity(ItemStack stack);

    double getPurityRate(ItemStack stack);
}
