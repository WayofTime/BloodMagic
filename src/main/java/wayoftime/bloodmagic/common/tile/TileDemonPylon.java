package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillConduit;
import wayoftime.bloodmagic.common.tile.base.TileTicking;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class TileDemonPylon extends TileTicking implements IDemonWillConduit
{
	public final int maxWill = 100;
	public final double drainRate = 1;
	public DemonWillHolder holder = new DemonWillHolder();

	public TileDemonPylon(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDemonPylon(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.DEMON_PYLON_TYPE.get(), pos, state);
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
	public void deserialize(CompoundTag tag)
	{
		holder.readFromNBT(tag, "Will");
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
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
