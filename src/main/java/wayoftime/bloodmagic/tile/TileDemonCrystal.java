package wayoftime.bloodmagic.tile;

import java.util.Locale;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.tile.base.TileTicking;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class TileDemonCrystal extends TileTicking
{
	public static final double sameWillConversionRate = 45;
	public static final double defaultWillConversionRate = 90;
	public static final double timeDelayForWrongWill = 0.6;
	public final int maxWill = 100;
	public DemonWillHolder holder = new DemonWillHolder();
	public double progressToNextCrystal = 0;
	public int internalCounter = 0;
	public Direction placement = Direction.UP; // Side that this crystal is placed on.

	public double injectedWill = 0;
	public double speedModifier = 1;
	public double appliedConversionRate = 45;

	public EnumDemonWillType willType;

	@ObjectHolder("bloodmagic:demoncrystal")
	public static TileEntityType<TileDemonCrystal> TYPE;

	public TileDemonCrystal(TileEntityType<?> type, EnumDemonWillType willType)
	{
		super(type);
		this.willType = willType;
	}

	public TileDemonCrystal(EnumDemonWillType willType)
	{
		this(TYPE, willType);
	}

	public TileDemonCrystal()
	{
		this(TYPE, EnumDemonWillType.DEFAULT);
	}

	@Override
	public void onUpdate()
	{
		if (level.isClientSide)
		{
			return;
		}

		internalCounter++;

		if (internalCounter % 20 == 0)
		{
			int crystalCount = getCrystalCount();
			if (crystalCount < 7)
			{
				EnumDemonWillType type = getWillType();

				double value = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, type);

				if (value >= 0.5)
				{
					double nextProgress = getCrystalGrowthPerSecond(value);

					double bufferDrainRate = (sameWillConversionRate - appliedConversionRate);
					double conversionRate = Math.min(appliedConversionRate, sameWillConversionRate);

					if (injectedWill > 0 && bufferDrainRate > 0)
					{
						nextProgress = Math.min(injectedWill / bufferDrainRate, nextProgress);
					}

					nextProgress = Math.min(WorldDemonWillHandler.drainWill(getLevel(), getBlockPos(), type, nextProgress * conversionRate, true) / conversionRate, nextProgress);
					progressToNextCrystal += nextProgress;

					if (injectedWill > 0 && bufferDrainRate > 0)
					{
						injectedWill = Math.max(0, injectedWill - nextProgress * bufferDrainRate);
						if (injectedWill <= 0)
						{
							appliedConversionRate = sameWillConversionRate;
							speedModifier = 1;
						}
					}
				} else if (type != EnumDemonWillType.DEFAULT)
				{
					// Does not use the injectedWill if it's not the same type
					value = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, EnumDemonWillType.DEFAULT);
					if (value > 0.5)
					{
						double nextProgress = getCrystalGrowthPerSecond(value) * timeDelayForWrongWill;
						progressToNextCrystal += WorldDemonWillHandler.drainWill(getLevel(), getBlockPos(), EnumDemonWillType.DEFAULT, nextProgress * defaultWillConversionRate, true) / defaultWillConversionRate;
					}
				}

				if (speedModifier <= 0)
				{
					speedModifier = 1;
				}

				checkAndGrowCrystal();
			}
		}

	}

	public double getInjectedWill()
	{
		return injectedWill;
	}

	public void applyCatalyst(double addedInjectedWill, double speedModifier, double conversionRate)
	{
		if (this.speedModifier < speedModifier)
		{
			this.speedModifier = speedModifier;
		}

		if (this.appliedConversionRate > conversionRate)
		{
			this.appliedConversionRate = conversionRate;
		}

		injectedWill += addedInjectedWill;
	}

	/**
	 * Encourages the crystal to grow by a large percentage by telling it to drain
	 * will from the aura.
	 *
	 * @param willDrain          The amount of drain that is needed for the crystal
	 *                           to grow successfully for the desired amount. Can be
	 *                           more than the base amount.
	 * @param progressPercentage
	 * @return percentage actually grown.
	 */
	public double growCrystalWithWillAmount(double willDrain, double progressPercentage)
	{
		int crystalCount = getCrystalCount();
		if (crystalCount >= 7)
		{
			return 0;
		}

		EnumDemonWillType type = this.getWillType();

		double value = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, type);
		double percentDrain = willDrain <= 0 ? 1 : Math.min(1, value / willDrain);
		if (percentDrain <= 0)
		{
			return 0;
		}

		// Verification that you can actually drain the will from this chunk, for future
		// proofing.
		WorldDemonWillHandler.drainWill(getLevel(), worldPosition, type, percentDrain * willDrain, true);
		progressToNextCrystal += percentDrain * progressPercentage;

		checkAndGrowCrystal();

		return percentDrain * progressPercentage;
	}

	public EnumDemonWillType getWillType()
	{
		return willType;
	}

	public void checkAndGrowCrystal()
	{
		int crystalCount = getCrystalCount();
		if (progressToNextCrystal >= 1 && internalCounter % 100 == 0 && crystalCount < 7)
		{
			progressToNextCrystal--;
			this.setCrystalCount(crystalCount + 1);
			setChanged();
			notifyUpdate();
		}
	}

	public double getMaxWillForCrystal()
	{
		return 50;
	}

	public boolean dropSingleCrystal()
	{
		int crystalCount = getCrystalCount();
		if (!getLevel().isClientSide && crystalCount > 1)
		{
			EnumDemonWillType type = getWillType();
//			EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
			ItemStack stack = BlockDemonCrystal.getItemStackDropped(type, 1);
			if (!stack.isEmpty())
			{
				setCrystalCount(crystalCount - 1);
				InventoryHelper.dropItemStack(getLevel(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
				notifyUpdate();
				return true;
			}
		}

		return false;
	}

	public double getCrystalGrowthPerSecond(double will)
	{
		double speed = 1.0 / 200 * Math.sqrt(will / 200);
		if (speedModifier > 0)
		{
			speed *= speedModifier;
		}

		return speed;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		holder.readFromNBT(tag, "Will");
		placement = Direction.from3DDataValue(tag.getInt("placement"));
		progressToNextCrystal = tag.getDouble("progress");

		if (!tag.contains(Constants.NBT.WILL_TYPE))
		{
			this.willType = EnumDemonWillType.DEFAULT;
		} else
		{
			this.willType = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ROOT));
		}

		injectedWill = tag.getDouble(Constants.NBT.INJECTED_WILL);
		speedModifier = tag.getDouble(Constants.NBT.SPEED_MODIFIER);
		appliedConversionRate = tag.getDouble(Constants.NBT.APPLIED_RATE);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		holder.writeToNBT(tag, "Will");
		tag.putInt("placement", placement.get3DDataValue());
		tag.putDouble("progress", progressToNextCrystal);

		if (willType == EnumDemonWillType.DEFAULT)
		{
			if (tag.contains(Constants.NBT.WILL_TYPE))
			{
				tag.remove(Constants.NBT.WILL_TYPE);
			}

		} else
		{
			tag.putString(Constants.NBT.WILL_TYPE, willType.toString());
		}

		tag.putDouble(Constants.NBT.INJECTED_WILL, injectedWill);
		tag.putDouble(Constants.NBT.SPEED_MODIFIER, speedModifier);
		tag.putDouble(Constants.NBT.APPLIED_RATE, appliedConversionRate);

		return tag;
	}

	public int getCrystalCount()
	{
		BlockState state = level.getBlockState(getBlockPos());
		return state.getValue(BlockDemonCrystal.AGE) + 1;
	}

	public void setCrystalCount(int crystalCount)
	{
		BlockState state = level.getBlockState(getBlockPos());
		level.setBlockAndUpdate(getBlockPos(), state.setValue(BlockDemonCrystal.AGE, crystalCount - 1));
	}

	public Direction getPlacement()
	{
		return placement;
	}

	public void setPlacement(Direction placement)
	{
		this.placement = placement;
	}

//	@Override
//	protected void onDataPacketClientReceived()
//	{
//		super.onDataPacketClientReceived();
//		notifyUpdate();
//	}
}
