package wayoftime.bloodmagic.tile;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.tile.base.TileTicking;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.will.EnumDemonWillType;

public class TileDemonCrystal extends TileTicking
{
	public static final double sameWillConversionRate = 50;
	public static final double defaultWillConversionRate = 100;
	public static final double timeDelayForWrongWill = 0.6;
	public final int maxWill = 100;
	public final double drainRate = 1;
	public DemonWillHolder holder = new DemonWillHolder();
	public double progressToNextCrystal = 0;
	public int internalCounter = 0;
	public Direction placement = Direction.UP; // Side that this crystal is placed on.

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
		if (getWorld().isRemote)
		{
			return;
		}

		internalCounter++;

		if (internalCounter % 20 == 0)
		{
			int crystalCount = getCrystalCount();
			if (crystalCount < 7)
			{
//			this.setCrystalCount(crystalCount + 1);
				EnumDemonWillType type = getWillType();

				double value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
				if (type != EnumDemonWillType.DEFAULT)
				{
					if (value >= 0.5)
					{
						double nextProgress = getCrystalGrowthPerSecond(value);
						progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), type, nextProgress
								* sameWillConversionRate, true) / sameWillConversionRate;
					} else
					{
						value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, EnumDemonWillType.DEFAULT);
						if (value > 0.5)
						{
							double nextProgress = getCrystalGrowthPerSecond(value) * timeDelayForWrongWill;
							progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), EnumDemonWillType.DEFAULT, nextProgress
									* defaultWillConversionRate, true) / defaultWillConversionRate;
						}
					}
				} else
				{
					if (value > 0.5)
					{

						double nextProgress = getCrystalGrowthPerSecond(value);
						progressToNextCrystal += WorldDemonWillHandler.drainWill(getWorld(), getPos(), type, nextProgress
								* sameWillConversionRate, true) / sameWillConversionRate;
					}
				}

				checkAndGrowCrystal();
			}
		}

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

		double value = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
		double percentDrain = willDrain <= 0 ? 1 : Math.min(1, value / willDrain);
		if (percentDrain <= 0)
		{
			return 0;
		}

		// Verification that you can actually drain the will from this chunk, for future
		// proofing.
		WorldDemonWillHandler.drainWill(getWorld(), pos, type, percentDrain * willDrain, true);
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
			markDirty();
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
		if (!getWorld().isRemote && crystalCount > 1)
		{
			BlockState state = getWorld().getBlockState(pos);
			EnumDemonWillType type = getWillType();
//			EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
			ItemStack stack = BlockDemonCrystal.getItemStackDropped(type, 1);
			if (!stack.isEmpty())
			{
				setCrystalCount(crystalCount - 1);
				InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
				notifyUpdate();
				return true;
			}
		}

		return false;
	}

	public double getCrystalGrowthPerSecond(double will)
	{
		return 1.0 / 200 * Math.sqrt(will / 200);
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		holder.readFromNBT(tag, "Will");
		placement = Direction.byIndex(tag.getInt("placement"));
		progressToNextCrystal = tag.getDouble("progress");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		holder.writeToNBT(tag, "Will");
		tag.putInt("placement", placement.getIndex());
		tag.putDouble("progress", progressToNextCrystal);
		return tag;
	}

	public int getCrystalCount()
	{
		BlockState state = world.getBlockState(getPos());
		return state.get(BlockDemonCrystal.AGE);
	}

	public void setCrystalCount(int crystalCount)
	{
		BlockState state = world.getBlockState(getPos());
		world.setBlockState(getPos(), state.with(BlockDemonCrystal.AGE, crystalCount - 1));
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
