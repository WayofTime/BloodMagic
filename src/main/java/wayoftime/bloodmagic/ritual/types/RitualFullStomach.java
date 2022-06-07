package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("full_stomach")
public class RitualFullStomach extends Ritual
{
	public static final String FILL_RANGE = "fillRange";
	public static final String CHEST_RANGE = "chest";

	public int foodLevel = 0;
	public float storedSaturation = 0;

	public RitualFullStomach()
	{
		super("ritualFullStomach", 0, 100000, "ritual." + BloodMagic.MODID + ".fullStomachRitual");
		addBlockRange(FILL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-25, -25, -25), 51));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(FILL_RANGE, 0, 25, 25);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		if (getRefreshCost() > currentEssence)
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
		if (tile == null)
			return;

		IItemHandler inventory = Utils.getInventory(tile, null);
		if (inventory == null)
			return;

		int lastSlot = 0;
		AreaDescriptor fillingRange = masterRitualStone.getBlockRange(FILL_RANGE);
		List<PlayerEntity> playerList = world.getEntitiesWithinAABB(PlayerEntity.class, fillingRange.getAABB(pos));

		// Check contained food level. If 0, grab a new food item to restock.

		if (foodLevel <= 0)
		{
			for (int i = lastSlot; i < inventory.getSlots(); i++)
			{
				ItemStack stack = inventory.extractItem(i, 1, true);

				if (!stack.isEmpty() && stack.getItem().isFood())
				{
					Food food = stack.getItem().getFood();

					foodLevel = food.getHealing();
					storedSaturation = food.getSaturation();
					inventory.extractItem(i, 1, false);

					masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));

					break;
				}
			}

			if (foodLevel <= 0)
			{
				return;
			}
		}

		for (PlayerEntity player : playerList)
		{
			FoodStats foodStats = player.getFoodStats();
			float satLevel = foodStats.getSaturationLevel();
			float saturationAmount = storedSaturation * 1 * 2.0f;

			// Checks to make sure we're being efficient with the food and not wasting high
			// value foods
			// If the food provides more than the max saturation, we just accept it no
			// matter what if the player is low
			while ((saturationAmount + satLevel <= 20 || satLevel < 5) && foodLevel > 0)
			{
				foodStats.addStats(1, storedSaturation);
				satLevel = foodStats.getSaturationLevel();
				foodLevel--;
			}
		}
	}

	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		foodLevel = tag.getInt("foodLevel");
		storedSaturation = tag.getFloat("storedSaturation");
	}

	public void writeToNBT(CompoundNBT tag)
	{
		super.writeToNBT(tag);
		tag.putInt("foodLevel", foodLevel);
		tag.putFloat("storedSaturation", storedSaturation);
	}

	@Override
	public int getRefreshTime()
	{
		return 20;
	}

	@Override
	public int getRefreshCost()
	{
		return 100;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 3, 0, EnumRuneType.FIRE);
		addCornerRunes(components, 1, 0, EnumRuneType.AIR);
		addOffsetRunes(components, 1, 2, 0, EnumRuneType.AIR);
		addCornerRunes(components, 4, 0, EnumRuneType.WATER);
		addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualFullStomach();
	}
}