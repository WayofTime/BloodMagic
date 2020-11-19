package wayoftime.bloodmagic.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.tile.base.TileTicking;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.will.EnumDemonWillType;
import wayoftime.bloodmagic.api.will.IDemonWillConduit;

public class TileDemonCrystallizer extends TileTicking implements IDemonWillConduit
{
	@ObjectHolder("bloodmagic:demoncrystallizer")
	public static TileEntityType<TileDemonCrystallizer> TYPE;

	public static final int maxWill = 100;
	public static final double drainRate = 1;
	public static final double willToFormCrystal = 99;
	public static final double totalFormationTime = 1000;
	// The whole purpose of this block is to grow a crystal initially. The
	// acceleration and crystal growing is up to the crystal itself afterwards.
	public DemonWillHolder holder = new DemonWillHolder();
	public double internalCounter = 0;

	public TileDemonCrystallizer(TileEntityType<?> type)
	{
		super(type);
	}

	public TileDemonCrystallizer()
	{
		this(TYPE);
	}

	@Override
	public void onUpdate()
	{
		if (world.isRemote)
		{
			return;
		}

		BlockPos offsetPos = pos.offset(Direction.UP);
		if (getWorld().isAirBlock(offsetPos)) // Room for a crystal to grow
		{
			EnumDemonWillType highestType = WorldDemonWillHandler.getHighestDemonWillType(getWorld(), pos);
			double amount = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, highestType);
			if (amount >= willToFormCrystal)
			{
				internalCounter += getCrystalFormationRate(amount);
				if (internalCounter >= totalFormationTime)
				{
					if (WorldDemonWillHandler.drainWill(getWorld(), getPos(), highestType, willToFormCrystal, false) >= willToFormCrystal)
					{
						if (formCrystal(highestType, offsetPos))
						{
							WorldDemonWillHandler.drainWill(getWorld(), getPos(), highestType, willToFormCrystal, true);
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
		getWorld().setBlockState(position, block.getDefaultState());
		TileEntity tile = getWorld().getTileEntity(position);
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
	public void deserialize(CompoundNBT tag)
	{
		holder.readFromNBT(tag, "Will");
		internalCounter = tag.getDouble("internalCounter");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
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
