package wayoftime.bloodmagic.util.helper;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.ritual.CapabilityRuneType;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;
import wayoftime.bloodmagic.ritual.IRitualStoneTile;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;

public class RitualHelper
{
	public static boolean canCrystalActivate(Ritual ritual, int crystalLevel)
	{
		return ritual.getCrystalLevel() <= crystalLevel && BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(ritual), false);
	}

	/**
	 * Checks the RitualRegistry to see if the configuration of the ritual stones in
	 * the world is valid for the given Direction.
	 *
	 * @param world - The world
	 * @param pos   - Location of the MasterRitualStone
	 * @return The ID of the valid ritual
	 */
	public static String getValidRitual(Level world, BlockPos pos)
	{
		for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals())
		{
			for (int i = 0; i < 4; i++)
			{
				Direction direction = Direction.from2DDataValue(i);

				if (checkValidRitual(world, pos, ritual, direction))
					return BloodMagic.RITUAL_MANAGER.getId(ritual);
			}
		}

		return "";
	}

	public static Direction getDirectionOfRitual(Level world, BlockPos pos, Ritual ritual)
	{
		for (int i = 0; i < 4; i++)
		{
			Direction direction = Direction.from2DDataValue(i);
			if (checkValidRitual(world, pos, ritual, direction))
				return direction;
		}

		return null;
	}

	public static boolean checkValidRitual(Level world, BlockPos pos, Ritual ritual, Direction direction)
	{
		if (ritual == null)
		{
			return false;
		}

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		for (RitualComponent component : components)
		{
			BlockPos newPos = pos.offset(component.getOffset(direction));
			if (!isRuneType(world, newPos, component.getRuneType()))
				return false;
		}

		return true;
	}

	public static boolean isRuneType(Level world, BlockPos pos, EnumRuneType type)
	{
		if (world == null)
			return false;
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity tile = world.getBlockEntity(pos);

		if (block instanceof IRitualStone)
			return ((IRitualStone) block).isRuneType(world, pos, type);
		else if (tile instanceof IRitualStoneTile)
			return ((IRitualStoneTile) tile).isRuneType(type);
		else if (tile != null && tile.getCapability(CapabilityRuneType.INSTANCE, null).isPresent())
			return tile.getCapability(CapabilityRuneType.INSTANCE, null).resolve().get().isRuneType(type);

		return false;
	}

	public static boolean isRune(Level world, BlockPos pos)
	{
		if (world == null)
			return false;
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity tile = world.getBlockEntity(pos);

		if (block instanceof IRitualStone)
			return true;
		else if (tile instanceof IRitualStoneTile)
			return true;
		else
			return tile != null && tile.getCapability(CapabilityRuneType.INSTANCE, null).isPresent();

	}

	public static void setRuneType(Level world, BlockPos pos, EnumRuneType type)
	{
		if (world == null)
			return;
		BlockState state = world.getBlockState(pos);
		BlockEntity tile = world.getBlockEntity(pos);

		if (state.getBlock() instanceof IRitualStone)
			((IRitualStone) state.getBlock()).setRuneType(world, pos, type);
		else if (tile instanceof IRitualStoneTile)
			((IRitualStoneTile) tile).setRuneType(type);
		else
		{
			LazyOptional<CapabilityRuneType> cap = tile.getCapability(CapabilityRuneType.INSTANCE, null);
			if (cap.isPresent())
			{
				cap.resolve().get().setRuneType(type);
				world.sendBlockUpdated(pos, state, state, 3);
			}

		}
	}

	public static boolean createRitual(Level world, BlockPos pos, Direction direction, Ritual ritual, boolean safe)
	{

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		if (abortConstruction(world, pos, direction, safe, components))
			return false;

		BlockState mrs = BloodMagicBlocks.MASTER_RITUAL_STONE.get().defaultBlockState();
		world.setBlockAndUpdate(pos, mrs);

		setRitualStones(direction, world, pos, components);
		return true;
	}

	public static boolean abortConstruction(Level world, BlockPos pos, Direction direction, boolean safe, List<RitualComponent> components)
	{
		// TODO: can be optimized to check only for the first and last component if
		// every ritual has those at the highest and lowest y-level respectivly.
		for (RitualComponent component : components)
		{
			BlockPos offset = component.getOffset(direction);
			BlockPos newPos = pos.offset(offset);
			if (world.isOutsideBuildHeight(newPos) || (safe && !world.isEmptyBlock(newPos)))
				return true;
		}
		return false;
	}

	public static boolean repairRitualFromRuins(TileMasterRitualStone tile, boolean safe)
	{
		Ritual ritual = tile.getCurrentRitual();
		Direction direction;
		Pair<Ritual, Direction> pair;
		if (ritual == null)
		{
			pair = getRitualFromRuins(tile);
			ritual = pair.getKey();
			direction = pair.getValue();
		} else
			direction = tile.getDirection();

		Level world = tile.getLevel();
		BlockPos pos = tile.getBlockPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		if (abortConstruction(world, pos, direction, safe, components))
			return false;

		setRitualStones(direction, world, pos, components);
		return true;
	}

	public static void setRitualStones(Direction direction, Level world, BlockPos pos, List<RitualComponent> gatheredComponents)
	{
		for (RitualComponent component : gatheredComponents)
		{
			BlockPos offset = component.getOffset(direction);
			BlockPos newPos = pos.offset(offset);
			((BlockRitualStone) BloodMagicBlocks.BLANK_RITUAL_STONE.get()).setRuneType(world, newPos, component.getRuneType());
		}
	}

	public static Pair<Ritual, Direction> getRitualFromRuins(TileMasterRitualStone tile)
	{
		BlockPos pos = tile.getBlockPos();
		Level world = tile.getLevel();
		Ritual possibleRitual = tile.getCurrentRitual();
		Direction possibleDirection = tile.getDirection();
		int highestCount = 0;

		if (possibleRitual == null || possibleDirection == null)
			for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals())
			{
				for (int i = 0; i < 4; i++)
				{
					Direction direction = Direction.from2DDataValue(i);
					List<RitualComponent> components = Lists.newArrayList();
					ritual.gatherComponents(components::add);
					int currentCount = 0;

					for (RitualComponent component : components)
					{
						BlockPos newPos = pos.offset(component.getOffset(direction));
						if (isRuneType(world, newPos, component.getRuneType()))
							currentCount += 1;
					}
					if (currentCount > highestCount)
					{
						highestCount = currentCount;
						possibleRitual = ritual;
						possibleDirection = direction;
					}

				}

			}
		return Pair.of(possibleRitual, possibleDirection);
	}

	/**
	 * Counts the number of each type of Ritual Stone.
	 * 
	 * @param ritual - ritual ID to count.
	 * @return Tuple(A, B). A = Total number of Ritual Stones. B = a Map of
	 *         EnumRuneType with that type's count.
	 */
	public static Tuple<Integer, Map<EnumRuneType, Integer>> countRunes(Ritual ritual)
	{
		Map<EnumRuneType, Integer> runeMap = new EnumMap<>(EnumRuneType.class);
		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		int totalRunes = components.size();
		for (RitualComponent component : components)
		{
			runeMap.compute(component.getRuneType(), (k, v) -> v == null ? 1 : v + 1);
		}
		return new Tuple<Integer, Map<EnumRuneType, Integer>>(totalRunes, runeMap);
	}
}
