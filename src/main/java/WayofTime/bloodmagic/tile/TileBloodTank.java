package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.tile.base.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileBloodTank extends TileBase {
    public static final int[] CAPACITIES = {16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65336, 131072, 262144, 524288};
    public int capacity;
    protected FluidTank tank;

    public TileBloodTank(int meta) {
        capacity = CAPACITIES[meta] * Fluid.BUCKET_VOLUME;
        tank = new FluidTank(capacity);
    }

    public TileBloodTank() {
        capacity = CAPACITIES[0] * Fluid.BUCKET_VOLUME;
        tank = new FluidTank(capacity);
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        super.deserialize(tagCompound);
        tank.readFromNBT(tagCompound.getCompoundTag(Constants.NBT.TANK));
        capacity = tagCompound.getInteger(Constants.NBT.ALTAR_CAPACITY);
        tank.setCapacity(capacity);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        super.serialize(tagCompound);
        if (tank.getFluidAmount() != 0)
            tagCompound.setTag(Constants.NBT.TANK, tank.writeToNBT(new NBTTagCompound()));
        tagCompound.setInteger(Constants.NBT.ALTAR_CAPACITY, capacity);
        return tagCompound;
    }

    public int getCapacity() {
        return capacity;
    }

    public FluidTank getTank() {
        return tank;
    }

    public Fluid getClientRenderFluid() {
        if (tank != null && tank.getFluid() != null)
            return tank.getFluid().getFluid();
        return null;
    }

    public float getRenderHeight() {
        if (tank != null && tank.getFluidAmount() > 0)
            return (float) tank.getFluidAmount() / (float) getCapacity();
        return 0F;
    }

    public int getComparatorOutput() {
        return tank.getFluidAmount() > 0 ? (int) (1 + ((double) tank.getFluidAmount() / (double) tank.getCapacity()) * 14) : 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }
}
