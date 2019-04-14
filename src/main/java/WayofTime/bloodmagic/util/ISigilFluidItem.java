package WayofTime.bloodmagic.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ISigilFluidItem {
    FluidStack getFluid(ItemStack sigil);

    int getCapacity(ItemStack sigil);

    int fill(ItemStack sigil, FluidStack resource, boolean doFill);

    FluidStack drain(ItemStack sigil, int maxDrain, boolean doDrain);
}