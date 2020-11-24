package wayoftime.bloodmagic.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.altar.IBloodAltar;

public class TileAltar extends TileInventory implements IBloodAltar, ITickableTileEntity
{
	@ObjectHolder("bloodmagic:altar")
	public static TileEntityType<TileAltar> TYPE;
	private BloodAltar bloodAltar;

	public TileAltar(TileEntityType<?> type)
	{
		super(type, 1, "altar");
		this.bloodAltar = new BloodAltar(this);
	}

	public TileAltar()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tagCompound)
	{
		super.deserialize(tagCompound);

		CompoundNBT altarTag = tagCompound.getCompound("bloodAltar");

		this.bloodAltar.readFromNBT(altarTag);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tagCompound)
	{
		super.serialize(tagCompound);

		CompoundNBT altarTag = new CompoundNBT();
		this.bloodAltar.writeToNBT(altarTag);

		tagCompound.put("bloodAltar", altarTag);
		return tagCompound;
	}

	@Override
	public void tick()
	{
		bloodAltar.update();
	}

	@Override
	public void sacrificialDaggerCall(int amount, boolean isSacrifice)
	{
		bloodAltar.sacrificialDaggerCall(amount, isSacrifice);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return slot == 0;
	}

	@Override
	public int getCapacity()
	{
		return bloodAltar.getCapacity();
	}

	@Override
	public int getCurrentBlood()
	{
		return bloodAltar.getCurrentBlood();
	}

	@Override
	public int getTier()
	{
		return bloodAltar.getTier().toInt();
	}

	@Override
	public int getProgress()
	{
		return bloodAltar.getProgress();
	}

	@Override
	public float getSacrificeMultiplier()
	{
		return bloodAltar.getSacrificeMultiplier();
	}

	@Override
	public float getSelfSacrificeMultiplier()
	{
		return bloodAltar.getSelfSacrificeMultiplier();
	}

	@Override
	public float getOrbMultiplier()
	{
		return bloodAltar.getOrbMultiplier();
	}

	@Override
	public float getDislocationMultiplier()
	{
		return bloodAltar.getDislocationMultiplier();
	}

	@Override
	public float getConsumptionMultiplier()
	{
		return bloodAltar.getConsumptionMultiplier();
	}

	@Override
	public float getConsumptionRate()
	{
		return bloodAltar.getConsumptionRate();
	}

	@Override
	public int getLiquidRequired()
	{
		return bloodAltar.getLiquidRequired();
	}

	@Override
	public int getBufferCapacity()
	{
		return bloodAltar.getBufferCapacity();
	}

	@Override
	public void startCycle()
	{
		bloodAltar.startCycle();
	}

	@Override
	public void checkTier()
	{
		bloodAltar.checkTier();
	}

	@Override
	public void requestPauseAfterCrafting(int cooldown)
	{
		bloodAltar.requestPauseAfterCrafting(cooldown);
	}

	@Override
	public boolean isActive()
	{
		return bloodAltar.isActive();
	}

	@Override
	public int fillMainTank(int amount)
	{
		return bloodAltar.fillMainTank(amount);
	}

	@Override
	public void setActive()
	{
		bloodAltar.setActive();
	}

	@Override
	public int getChargingRate()
	{
		return bloodAltar.getChargingRate();
	}

	@Override
	public int getTotalCharge()
	{
		return bloodAltar.getTotalCharge();
	}

	@Override
	public int getChargingFrequency()
	{
		return bloodAltar.getChargingFrequency();
	}

	public AltarTier getCurrentTierDisplayed()
	{
		return bloodAltar.getCurrentTierDisplayed();
	}

	public boolean setCurrentTierDisplayed(AltarTier altarTier)
	{
		return bloodAltar.setCurrentTierDisplayed(altarTier);
	}
//
//	@Override
//	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing)
//	{
//		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//		{
//			return true;
//		}
//
//		return super.hasCapability(capability, facing);
//	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> new BloodAltar.VariableSizeFluidHandler(bloodAltar)).cast();
//			return (T) bloodAltar;
		}

		return super.getCapability(capability, facing);
	}
}
