package wayoftime.bloodmagic.util.helper;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;
import wayoftime.bloodmagic.ritual.IRitualStone.Tile;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;

public class RitualHelper
{
	@CapabilityInject(IRitualStone.Tile.class)
	static Capability<IRitualStone.Tile> RUNE_CAPABILITY = null;

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
	public static String getValidRitual(World world, BlockPos pos)
	{
		for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals())
		{
			for (int i = 0; i < 4; i++)
			{
				Direction direction = Direction.byHorizontalIndex(i);

				if (checkValidRitual(world, pos, ritual, direction))
					return BloodMagic.RITUAL_MANAGER.getId(ritual);
			}
		}

		return "";
	}

	public static Direction getDirectionOfRitual(World world, BlockPos pos, Ritual ritual)
	{
		for (int i = 0; i < 4; i++)
		{
			Direction direction = Direction.byHorizontalIndex(i);
			if (checkValidRitual(world, pos, ritual, direction))
				return direction;
		}

		return null;
	}

	public static boolean checkValidRitual(World world, BlockPos pos, Ritual ritual, Direction direction)
	{
		if (ritual == null)
		{
			return false;
		}

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		for (RitualComponent component : components)
		{
			BlockPos newPos = pos.add(component.getOffset(direction));
			if (!isRuneType(world, newPos, component.getRuneType()))
				return false;
		}

		return true;
	}

	public static boolean isRuneType(World world, BlockPos pos, EnumRuneType type)
	{
		if (world == null)
			return false;
		Block block = world.getBlockState(pos).getBlock();
		TileEntity tile = world.getTileEntity(pos);

		if (block instanceof IRitualStone)
			return ((IRitualStone) block).isRuneType(world, pos, type);
		else if (tile instanceof IRitualStone.Tile)
			return ((IRitualStone.Tile) tile).isRuneType(type);
		else if (tile != null && tile.getCapability(RUNE_CAPABILITY, null).isPresent())
			return tile.getCapability(RUNE_CAPABILITY, null).resolve().get().isRuneType(type);

		return false;
	}

	public static boolean isRune(World world, BlockPos pos)
	{
		if (world == null)
			return false;
		Block block = world.getBlockState(pos).getBlock();
		TileEntity tile = world.getTileEntity(pos);

		if (block instanceof IRitualStone)
			return true;
		else if (tile instanceof IRitualStone.Tile)
			return true;
		else
			return tile != null && tile.getCapability(RUNE_CAPABILITY, null).isPresent();

	}

	public static void setRuneType(World world, BlockPos pos, EnumRuneType type)
	{
		if (world == null)
			return;
		BlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);

		if (state.getBlock() instanceof IRitualStone)
			((IRitualStone) state.getBlock()).setRuneType(world, pos, type);
		else if (tile instanceof IRitualStone.Tile)
			((IRitualStone.Tile) tile).setRuneType(type);
		else
		{
			LazyOptional<Tile> cap = tile.getCapability(RUNE_CAPABILITY, null);
			if (cap.isPresent())
			{
				cap.resolve().get().setRuneType(type);
				world.notifyBlockUpdate(pos, state, state, 3);
			}

		}
	}

	public static boolean createRitual(World world, BlockPos pos, Direction direction, Ritual ritual, boolean safe)
	{

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		if (abortConstruction(world, pos, direction, safe, components))
			return false;

		BlockState mrs = BloodMagicBlocks.MASTER_RITUAL_STONE.get().getDefaultState();
		world.setBlockState(pos, mrs);

		setRitualStones(direction, world, pos, components);
		return true;
	}

	public static boolean abortConstruction(World world, BlockPos pos, Direction direction, boolean safe, List<RitualComponent> components)
	{
		// TODO: can be optimized to check only for the first and last component if
		// every ritual has those at the highest and lowest y-level respectivly.
		for (RitualComponent component : components)
		{
			BlockPos offset = component.getOffset(direction);
			BlockPos newPos = pos.add(offset);
			if (world.isOutsideBuildHeight(newPos) || (safe && !world.isAirBlock(newPos)))
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

		World world = tile.getWorld();
		BlockPos pos = tile.getPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);

		if (abortConstruction(world, pos, direction, safe, components))
			return false;

		setRitualStones(direction, world, pos, components);
		return true;
	}

	public static void setRitualStones(Direction direction, World world, BlockPos pos, List<RitualComponent> gatheredComponents)
	{
		for (RitualComponent component : gatheredComponents)
		{
			BlockPos offset = component.getOffset(direction);
			BlockPos newPos = pos.add(offset);
			((BlockRitualStone) BloodMagicBlocks.BLANK_RITUAL_STONE.get()).setRuneType(world, newPos, component.getRuneType());
		}
	}

	public static Pair<Ritual, Direction> getRitualFromRuins(TileMasterRitualStone tile)
	{
		BlockPos pos = tile.getPos();
		World world = tile.getWorld();
		Ritual possibleRitual = tile.getCurrentRitual();
		Direction possibleDirection = tile.getDirection();
		int highestCount = 0;

		if (possibleRitual == null || possibleDirection == null)
			for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals())
			{
				for (int i = 0; i < 4; i++)
				{
					Direction direction = Direction.byHorizontalIndex(i);
					List<RitualComponent> components = Lists.newArrayList();
					ritual.gatherComponents(components::add);
					int currentCount = 0;

					for (RitualComponent component : components)
					{
						BlockPos newPos = pos.add(component.getOffset(direction));
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
		int blankRunes = 0;
		int waterRunes = 0;
		int earthRunes = 0;
		int fireRunes = 0;
		int airRunes = 0;
		int duskRunes = 0;
		int dawnRunes = 0;
		int totalRunes = components.size();

		for (RitualComponent component : components)
		{
			switch (component.getRuneType())
			{
			case BLANK:
				blankRunes++;
				break;
			case WATER:
				waterRunes++;
				break;
			case EARTH:
				earthRunes++;
				break;
			case FIRE:
				fireRunes++;
				break;
			case AIR:
				airRunes++;
				break;
			case DUSK:
				duskRunes++;
				break;
			case DAWN:
				dawnRunes++;
				break;
			}
		}
		runeMap.put(EnumRuneType.BLANK, blankRunes);
		runeMap.put(EnumRuneType.WATER, waterRunes);
		runeMap.put(EnumRuneType.EARTH, earthRunes);
		runeMap.put(EnumRuneType.FIRE, fireRunes);
		runeMap.put(EnumRuneType.AIR, airRunes);
		runeMap.put(EnumRuneType.DUSK, duskRunes);
		runeMap.put(EnumRuneType.DAWN, dawnRunes);

		return new Tuple<Integer, Map<EnumRuneType, Integer>>(totalRunes, runeMap);
	}
}
