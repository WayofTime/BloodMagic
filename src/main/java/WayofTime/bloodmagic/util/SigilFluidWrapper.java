package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.item.sigil.ItemSigilFluidBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SigilFluidWrapper implements ICapabilityProvider {

    final ItemStack stack;
    final ItemSigilFluidBase sigil;
    final boolean canFill;
    final boolean canDrain;

    public SigilFluidWrapper(ItemStack stackIn, ItemSigilFluidBase fluidSigil) {
        stack = stackIn;
        sigil = fluidSigil;
        canFill = true;
        canDrain = true;
    }

    public SigilFluidWrapper(ItemStack stackIn, ItemSigilFluidBase fluidSigil, boolean canFillIn, boolean canDrainIn) {
        stack = stackIn;
        sigil = fluidSigil;
        canFill = canFillIn;
        canDrain = canDrainIn;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing from) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, final EnumFacing from) {
        if (!hasCapability(capability, from)) {
            return null;
        }
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(new IFluidHandlerItem() {

            @Override
            public IFluidTankProperties[] getTankProperties() {
                return new IFluidTankProperties[]{new FluidTankProperties(sigil.getFluid(stack), sigil.getCapacity(stack), canFill, canDrain)};
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                return 0;
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                return sigil.drain(stack, resource.amount, doDrain);
            }

            @Nullable
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                return sigil.drain(stack, maxDrain, doDrain);
            }

            @Nonnull
            @Override
            public ItemStack getContainer() {
                return stack;
            }

        });
    }

}