package wayoftime.bloodmagic.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.common.block.BlockAltar;

public class TileAltar extends TileInventory implements IBloodAltar, TickableBlockEntity
{
	@ObjectHolder("bloodmagic:altar")
	public static BlockEntityType<TileAltar> TYPE;
	private BloodAltar bloodAltar;

	private LazyOptional fluidOptional;
	private boolean isOutputOn;

	public TileAltar(BlockEntityType<?> type)
	{
		super(type, 1, "altar");
		this.bloodAltar = new BloodAltar(this);
		this.isOutputOn = false;
	}

	public TileAltar()
	{
		this(TYPE);
	}

	public boolean getOutputState()
	{
		return this.isOutputOn;
	}

	public void setOutputState(boolean state)
	{
		BlockAltar altar = (BlockAltar) this.getLevel().getBlockState(worldPosition).getBlock();

		this.isOutputOn = state;
		this.level.updateNeighborsAt(worldPosition, altar);
	}

	@Override
	public void deserialize(CompoundTag tagCompound)
	{
		super.deserialize(tagCompound);

		CompoundTag altarTag = tagCompound.getCompound("bloodAltar");

		this.bloodAltar.readFromNBT(altarTag);
	}

	@Override
	public CompoundTag serialize(CompoundTag tagCompound)
	{
		super.serialize(tagCompound);

		CompoundTag altarTag = new CompoundTag();
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
	public boolean canPlaceItem(int slot, ItemStack itemstack)
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
	public int getAnalogSignalStrength(int redstoneMode)
	{
		return bloodAltar.getAnalogSignalStrength(redstoneMode);
	}

	@Override
	protected void invalidateCaps()
	{
		super.invalidateCaps();
		if (fluidOptional != null)
		{
			fluidOptional.invalidate();
			fluidOptional = null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			if (fluidOptional == null)
			{
				fluidOptional = LazyOptional.of(() -> new BloodAltar.VariableSizeFluidHandler(bloodAltar));
			}
			return fluidOptional.cast();
//			return (T) bloodAltar;
		}

		return super.getCapability(capability, facing);
	}
}
