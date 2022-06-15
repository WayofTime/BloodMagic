package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillConduit;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.base.TileTicking;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class TileDemonCrystallizer extends TileTicking implements IDemonWillConduit
{
	public static final int maxWill = 100;
	public static final double drainRate = 1;
	public static final double willToFormCrystal = 99;
	public static final double totalFormationTime = 1000;
	// The whole purpose of this block is to grow a crystal initially. The
	// acceleration and crystal growing is up to the crystal itself afterwards.
	public DemonWillHolder holder = new DemonWillHolder();
	public double internalCounter = 0;

	public TileDemonCrystallizer(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDemonCrystallizer(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.DEMON_CRYSTALLIZER_TYPE.get(), pos, state);
	}

	@Override
	public void onUpdate()
	{
		if (level.isClientSide)
		{
			return;
		}

		BlockPos offsetPos = worldPosition.relative(Direction.UP);
		if (getLevel().isEmptyBlock(offsetPos)) // Room for a crystal to grow
		{
			EnumDemonWillType highestType = WorldDemonWillHandler.getHighestDemonWillType(getLevel(), worldPosition);
			double amount = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, highestType);
			if (amount >= willToFormCrystal)
			{
				internalCounter += getCrystalFormationRate(amount);
				if (internalCounter >= totalFormationTime)
				{
					if (WorldDemonWillHandler.drainWill(getLevel(), getBlockPos(), highestType, willToFormCrystal, false) >= willToFormCrystal)
					{
						if (formCrystal(highestType, offsetPos))
						{
							WorldDemonWillHandler.drainWill(getLevel(), getBlockPos(), highestType, willToFormCrystal, true);
							internalCounter = 0;
						}
					}
				}
			}
		}
	}

	public boolean formCrystal(EnumDemonWillType type, BlockPos position)
	{
		Block block = BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get();
		switch (type)
		{
		case CORROSIVE:
			block = BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get();
			break;
		case DESTRUCTIVE:
			block = BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get();
			break;
		case STEADFAST:
			block = BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get();
			break;
		case VENGEFUL:
			block = BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get();
			break;
		default:
			break;
		}
		getLevel().setBlockAndUpdate(position, block.defaultBlockState());
		BlockEntity tile = getLevel().getBlockEntity(position);
		if (tile instanceof TileDemonCrystal)
		{
			((TileDemonCrystal) tile).setPlacement(Direction.UP);
			return true;
		}

		return false;
	}

	public double getCrystalFormationRate(double currentWill)
	{
		return 1;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		holder.readFromNBT(tag, "Will");
		internalCounter = tag.getDouble("internalCounter");
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		holder.writeToNBT(tag, "Will");
		tag.putDouble("internalCounter", internalCounter);
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
