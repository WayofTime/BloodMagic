package wayoftime.bloodmagic.tile;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.api.will.EnumDemonWillType;
import wayoftime.bloodmagic.api.will.IDemonWillConduit;
import wayoftime.bloodmagic.api.will.IDemonWillGem;
import wayoftime.bloodmagic.api.will.IDiscreteDemonWill;

public class TileDemonCrucible extends TileInventory implements ITickableTileEntity, IDemonWillConduit, ISidedInventory
{
	@ObjectHolder("bloodmagic:demoncrucible")
	public static TileEntityType<TileDemonCrucible> TYPE;
	public final int maxWill = 100;
	public final double gemDrainRate = 10;
	public HashMap<EnumDemonWillType, Double> willMap = new HashMap<>(); // TODO: Change to DemonWillHolder
	public int internalCounter = 0;

	public TileDemonCrucible(TileEntityType<?> type)
	{
		super(type, 1, "demoncrucible");
	}

	public TileDemonCrucible()
	{
		this(TYPE);
	}

	@Override
	public void tick()
	{
		if (getWorld().isRemote)
		{
			return;
		}

		internalCounter++;

		if (getWorld().isBlockPowered(getPos()))
		{
			// TODO: Fill the contained gem if it is there.
			ItemStack stack = this.getStackInSlot(0);
			if (stack.getItem() instanceof IDemonWillGem)
			{
				IDemonWillGem gemItem = (IDemonWillGem) stack.getItem();
				for (EnumDemonWillType type : EnumDemonWillType.values())
				{
					if (willMap.containsKey(type))
					{
						double current = willMap.get(type);
						double fillAmount = Math.min(gemDrainRate, current);
						if (fillAmount > 0)
						{
							fillAmount = gemItem.fillWill(type, stack, fillAmount, true);
							if (willMap.get(type) - fillAmount <= 0)
							{
								willMap.remove(type);
							} else
							{
								willMap.put(type, willMap.get(type) - fillAmount);
							}
						}
					}
				}
			}
		} else
		{
			ItemStack stack = this.getStackInSlot(0);
			if (!stack.isEmpty())
			{
				if (stack.getItem() instanceof IDemonWillGem)
				{
					IDemonWillGem gemItem = (IDemonWillGem) stack.getItem();
					for (EnumDemonWillType type : EnumDemonWillType.values())
					{
						double currentAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
						double drainAmount = Math.min(maxWill - currentAmount, gemDrainRate);
						double filled = WorldDemonWillHandler.fillWillToMaximum(getWorld(), pos, type, drainAmount, maxWill, false);

						filled = gemItem.drainWill(type, stack, filled, false);
						if (filled > 0)
						{
							filled = gemItem.drainWill(type, stack, filled, true);
							WorldDemonWillHandler.fillWillToMaximum(getWorld(), pos, type, filled, maxWill, true);
						}
					}
				} else if (stack.getItem() instanceof IDiscreteDemonWill) // TODO: Limit the speed of this process
				{
					IDiscreteDemonWill willItem = (IDiscreteDemonWill) stack.getItem();
					EnumDemonWillType type = willItem.getType(stack);
					double currentAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
					double needed = maxWill - currentAmount;
					double discreteAmount = willItem.getDiscretization(stack);
					if (needed >= discreteAmount)
					{
						double filled = willItem.drainWill(stack, discreteAmount);
						if (filled > 0)
						{
							WorldDemonWillHandler.fillWillToMaximum(getWorld(), pos, type, filled, maxWill, true);
							if (stack.getCount() <= 0)
							{
								this.setInventorySlotContents(0, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		willMap.clear();

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			double amount = tag.getDouble("EnumWill" + type.name());
			if (amount > 0)
			{
				willMap.put(type, amount);
			}
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		for (Entry<EnumDemonWillType, Double> entry : willMap.entrySet())
		{
			tag.putDouble("EnumWill" + entry.getKey().name(), entry.getValue());
		}
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
			if (!willMap.containsKey(type))
			{
				return Math.min(maxWill, amount);
			}

			return Math.min(maxWill - willMap.get(type), amount);
		}

		if (!willMap.containsKey(type))
		{
			double max = Math.min(maxWill, amount);

			willMap.put(type, max);

			return max;
		}

		double current = willMap.get(type);
		double filled = maxWill - current;

		if (amount < filled)
		{
			willMap.put(type, current + amount);
			filled = amount;
		} else
		{
			willMap.put(type, (double) maxWill);
		}

		return filled;
	}

	@Override
	public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
	{
		if (!willMap.containsKey(type))
		{
			return 0;
		}

		double drained = amount;
		double current = willMap.get(type);
		if (current < drained)
		{
			drained = current;
		}

		if (doDrain)
		{
			current -= drained;
			if (current <= 0)
			{
				willMap.remove(type);
			} else
			{
				willMap.put(type, current);
			}
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
		return willMap.containsKey(type) ? willMap.get(type) : 0;
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return new int[]
		{ 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, Direction direction)
	{
		return !stack.isEmpty() && inventory.get(0).isEmpty()
				&& (stack.getItem() instanceof IDemonWillGem || stack.getItem() instanceof IDiscreteDemonWill);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		return true;
	}
}