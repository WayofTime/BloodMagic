package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("zephyr")
public class RitualZephyr extends Ritual
{
	public static final String ZEPHYR_RANGE = "zephyrRange";
	public static final String CHEST_RANGE = "chest";

	public RitualZephyr()
	{
		super("ritualZephyr", 0, 1000, "ritual." + BloodMagic.MODID + ".zephyrRitual");
		addBlockRange(ZEPHYR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(ZEPHYR_RANGE, 0, 10, 10);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));
		if (!masterRitualStone.getWorldObj().isRemote && tileInventory != null)
		{
			if (currentEssence < getRefreshCost())
			{
				masterRitualStone.getOwnerNetwork().causeNausea();
				return;
			}

			AreaDescriptor zephyrRange = masterRitualStone.getBlockRange(ZEPHYR_RANGE);

			List<ItemEntity> itemList = world.getEntitiesWithinAABB(ItemEntity.class, zephyrRange.getAABB(masterRitualStone.getMasterBlockPos()));
			int count = 0;

			for (ItemEntity entityItem : itemList)
			{
				if (!entityItem.isAlive())
				{
					continue;
				}

				ItemStack copyStack = entityItem.getItem().copy();
				int originalAmount = copyStack.getCount();
				ItemStack newStack = Utils.insertStackIntoTile(copyStack, tileInventory, Direction.DOWN);

				if (!newStack.isEmpty() && newStack.getCount() < originalAmount)
				{
					count++;
					if (newStack.isEmpty())
						entityItem.remove();

					entityItem.getItem().setCount(newStack.getCount());
				}
//
				if (newStack.isEmpty())
				{
					entityItem.remove();
				}
			}

			masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * Math.min(count, 100)));
		}
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public int getRefreshCost()
	{
		return 1;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 2, 0, EnumRuneType.AIR);
		addCornerRunes(components, 1, 1, EnumRuneType.AIR);
		addParallelRunes(components, 1, -1, EnumRuneType.AIR);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualZephyr();
	}
}