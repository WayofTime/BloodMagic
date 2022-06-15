package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemCrystalCatalyst;
import wayoftime.bloodmagic.common.tile.TileDemonCrystal;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("forsaken_soul")
public class RitualForsakenSoul extends Ritual
{
	public static final String CRYSTAL_RANGE = "crystal";
	public static final String CHEST_RANGE = "chest";

	public List<Integer> keyList = new ArrayList<>();

	public RitualForsakenSoul()
	{
		super("ritualForsakenSoul", 0, 40000, "ritual." + BloodMagic.MODID + ".forsakenSoulRitual");
		addBlockRange(CRYSTAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, -7, -3), 7, 5, 7));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 2, 0), 1));

		setMaximumVolumeAndDistanceOfRange(CRYSTAL_RANGE, 250, 5, 7);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		BlockPos pos = masterRitualStone.getMasterBlockPos();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		int maxEffects = Math.min(100, currentEssence / getRefreshCost());
		int totalEffects = 0;

		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		List<BlockPos> chestList = chestRange.getContainedPositions(pos);
		if (chestList.size() <= 0)
		{
			return;
		}

		BlockEntity tileInventory = world.getBlockEntity(chestList.get(0));

		if (tileInventory == null)
			return;

		IItemHandler inventory = Utils.getInventory(tileInventory, null);
		if (inventory == null)
			return;

		List<TileDemonCrystal> crystalList = new ArrayList<>();

		AreaDescriptor crystalRange = masterRitualStone.getBlockRange(CRYSTAL_RANGE);

		crystalRange.resetIterator();
		while (crystalRange.hasNext())
		{
			BlockPos nextPos = crystalRange.next().offset(pos);
			BlockEntity tile = world.getBlockEntity(nextPos);
			if (tile instanceof TileDemonCrystal)
			{
				crystalList.add((TileDemonCrystal) tile);
			}
		}

		Collections.shuffle(crystalList);

		for (int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemCrystalCatalyst)
			{
				stack = inventory.extractItem(i, stack.getCount(), true);
				int remainingCount = stack.getCount();
				for (TileDemonCrystal crystalTile : crystalList)
				{
					if (((ItemCrystalCatalyst) stack.getItem()).applyCatalyst(crystalTile))
					{
						remainingCount--;
						totalEffects++;

						if (remainingCount <= 0 || totalEffects >= maxEffects)
						{
							break;
						}
					}
				}

				int extractedCount = stack.getCount() - remainingCount;
				if (extractedCount > 0)
				{
					inventory.extractItem(i, extractedCount, false);
				}

				if (totalEffects >= maxEffects)
				{
					break;
				}
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
	}

	@Override
	public int getRefreshTime()
	{
		return 25;
	}

	@Override
	public int getRefreshCost()
	{
		return 2;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.AIR);
		addParallelRunes(components, 1, -1, EnumRuneType.DUSK);
		addParallelRunes(components, 1, 1, EnumRuneType.FIRE);
		addParallelRunes(components, 2, 1, EnumRuneType.FIRE);
		addParallelRunes(components, 3, 1, EnumRuneType.FIRE);
		addOffsetRunes(components, 3, 1, 1, EnumRuneType.FIRE);
		addCornerRunes(components, 3, 1, EnumRuneType.EARTH);
		addCornerRunes(components, 3, 0, EnumRuneType.EARTH);
		addOffsetRunes(components, 3, 2, 0, EnumRuneType.EARTH);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualForsakenSoul();
	}
}