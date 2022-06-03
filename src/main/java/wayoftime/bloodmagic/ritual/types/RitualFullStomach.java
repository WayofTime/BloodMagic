package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
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

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;

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

		for (PlayerEntity player : playerList)
		{
			FoodStats foodStats = player.getFoodStats();
			float satLevel = foodStats.getSaturationLevel();

			for (int i = lastSlot; i < inventory.getSlots(); i++)
			{
				ItemStack stack = inventory.extractItem(i, 1, true);

				if (!stack.isEmpty() && stack.getItem().isFood())
				{
					Food food = stack.getItem().getFood();

					int healAmount = food.getHealing();
					float saturationAmount = food.getSaturation() * healAmount * 2.0f;

					// Checks to make sure we're being efficient with the food and not wasting high
					// value foods
					// If the food provides more than the max saturation, we just accept it no
					// matter what if the player is low
					if (saturationAmount + satLevel <= 20 || satLevel < 5)
					{
						foodStats.addStats(healAmount, saturationAmount);
						inventory.extractItem(i, 1, false);
						totalEffects++;
						lastSlot = i;
						break;
					}
				}
			}

			if (totalEffects >= maxEffects)
			{
				masterRitualStone.getOwnerNetwork().causeNausea();
				break;
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
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