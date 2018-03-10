package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.IBloodAltar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAltar extends TileInventory implements IBloodAltar, ITickable {
    private BloodAltar bloodAltar;

    public TileAltar() {
        super(1, "altar");
        this.bloodAltar = new BloodAltar(this);
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        super.deserialize(tagCompound);

        NBTTagCompound altarTag = tagCompound.getCompoundTag("bloodAltar");

        this.bloodAltar.readFromNBT(altarTag);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        super.serialize(tagCompound);

        NBTTagCompound altarTag = new NBTTagCompound();
        this.bloodAltar.writeToNBT(altarTag);

        tagCompound.setTag("bloodAltar", altarTag);
        return tagCompound;
    }

    @Override
    public void update() {
        bloodAltar.update();
    }

    @Override
    public void sacrificialDaggerCall(int amount, boolean isSacrifice) {
        bloodAltar.sacrificialDaggerCall(amount, isSacrifice);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == 0;
    }

    @Override
    public int getCapacity() {
        return bloodAltar.getCapacity();
    }

    @Override
    public int getCurrentBlood() {
        return bloodAltar.getCurrentBlood();
    }

    @Override
    public AltarTier getTier() {
        return bloodAltar.getTier();
    }

    @Override
    public int getProgress() {
        return bloodAltar.getProgress();
    }

    @Override
    public float getSacrificeMultiplier() {
        return bloodAltar.getSacrificeMultiplier();
    }

    @Override
    public float getSelfSacrificeMultiplier() {
        return bloodAltar.getSelfSacrificeMultiplier();
    }

    @Override
    public float getOrbMultiplier() {
        return bloodAltar.getOrbMultiplier();
    }

    @Override
    public float getDislocationMultiplier() {
        return bloodAltar.getDislocationMultiplier();
    }

    @Override
    public float getConsumptionMultiplier() {
        return bloodAltar.getConsumptionMultiplier();
    }

    @Override
    public float getConsumptionRate() {
        return bloodAltar.getConsumptionRate();
    }

    @Override
    public int getLiquidRequired() {
        return bloodAltar.getLiquidRequired();
    }

    @Override
    public int getBufferCapacity() {
        return bloodAltar.getBufferCapacity();
    }

    @Override
    public void startCycle() {
        bloodAltar.startCycle();
    }

    @Override
    public void checkTier() {
        bloodAltar.checkTier();
    }

    @Override
    public void requestPauseAfterCrafting(int cooldown) {
        bloodAltar.requestPauseAfterCrafting(cooldown);
    }

    @Override
    public boolean isActive() {
        return bloodAltar.isActive();
    }

    @Override
    public int fillMainTank(int amount) {
        return bloodAltar.fillMainTank(amount);
    }

    @Override
    public void setActive() {
        bloodAltar.setActive();
    }

    @Override
    public int getChargingRate() {
        return bloodAltar.getChargingRate();
    }

    @Override
    public int getTotalCharge() {
        return bloodAltar.getTotalCharge();
    }

    @Override
    public int getChargingFrequency() {
        return bloodAltar.getChargingFrequency();
    }

    public AltarTier getCurrentTierDisplayed() {
        return bloodAltar.getCurrentTierDisplayed();
    }

    public boolean setCurrentTierDisplayed(AltarTier altarTier) {
        return bloodAltar.setCurrentTierDisplayed(altarTier);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) bloodAltar;
        }

        return super.getCapability(capability, facing);
    }
}