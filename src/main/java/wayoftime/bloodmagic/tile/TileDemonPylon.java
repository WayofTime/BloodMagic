package wayoftime.bloodmagic.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillConduit;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.tile.base.TileTicking;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class TileDemonPylon extends TileTicking implements IDemonWillConduit
{
	@ObjectHolder("bloodmagic:demonpylon")
	public static TileEntityType<TileDemonPylon> TYPE;

	public final int maxWill = 100;
	public final double drainRate = 1;
	public DemonWillHolder holder = new DemonWillHolder();

	public TileDemonPylon(TileEntityType<?> type)
	{
		super(type);
	}

	public TileDemonPylon()
	{
		this(TYPE);
	}

	@Override
	public void onUpdate()
	{
		if (level.isClientSide)
		{
			return;
		}

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			double currentAmount = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, type);

			for (int i = 0; i < 4; i++)
			{
				Direction side = Direction.from2DDataValue(i);
				BlockPos offsetPos = worldPosition.relative(side, 16);
				double sideAmount = WorldDemonWillHandler.getCurrentWill(getLevel(), offsetPos, type);
				if (sideAmount > currentAmount)
				{
					double drainAmount = Math.min((sideAmount - currentAmount) / 2, drainRate);
					double drain = WorldDemonWillHandler.drainWill(getLevel(), offsetPos, type, drainAmount, true);
					WorldDemonWillHandler.fillWill(getLevel(), worldPosition, type, drain, true);
				}
			}
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		holder.readFromNBT(tag, "Will");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		holder.writeToNBT(tag, "Will");
		return tag;
	}

	// IDemonWillConduit

	@Override
	public int getWeight()
	{
		return 10;
	}

	@Override
	public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill)
	{
		if (amount <= 0)
		{
			return 0;
		}

		if (!canFill(type))
		{
			return 0;
		}

		if (!doFill)
		{
			return Math.min(maxWill - holder.getWill(type), amount);
		}

		return holder.addWill(type, amount, maxWill);
	}

	@Override
	public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
	{
		double drained = amount;
		double current = holder.getWill(type);
		if (current < drained)
		{
			drained = current;
		}

		if (doDrain)
		{
			return holder.drainWill(type, amount);
		}

		return drained;
	}

	@Override
	public boolean canFill(EnumDemonWillType type)
	{
		return true;
	}

	@Override
	public boolean canDrain(EnumDemonWillType type)
	{
		return true;
	}

	@Override
	public double getCurrentWill(EnumDemonWillType type)
	{
		return holder.getWill(type);
	}
}
