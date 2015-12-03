package WayofTime.bloodmagic.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IBloodAltar;

public class TileAltar extends TileInventory implements IBloodAltar, ITickable, IFluidTank, IFluidHandler {

	private BloodAltar bloodAltar;
	
    public TileAltar() {
        super(1, "altar");
        this.bloodAltar = new BloodAltar(this);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagCompound altarTag = tagCompound.getCompoundTag("bloodAltar");
        
        this.bloodAltar.readFromNBT(altarTag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagCompound altarTag = new NBTTagCompound();
        this.bloodAltar.writeToNBT(altarTag);
        
        tagCompound.setTag("bloodAltar", altarTag);
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


    // IFluidHandler

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[0];
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    // IFluidTank

    @Override
    public FluidStack getFluid() {
        return bloodAltar.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return bloodAltar.getFluidAmount();
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
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
	public EnumAltarTier getTier() {
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
	public void setActive(){
		bloodAltar.setActive();
	}
}