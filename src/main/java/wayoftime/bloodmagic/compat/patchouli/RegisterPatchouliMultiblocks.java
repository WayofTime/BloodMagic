package wayoftime.bloodmagic.compat.patchouli;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.TriPredicate;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.PatchouliAPI.IPatchouliAPI;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.AltarComponent;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;

public class RegisterPatchouliMultiblocks
{
	private int maxX;
	private int minX;
	private int maxY;
	private int minY;
	private int maxZ;
	private int minZ;

	public RegisterPatchouliMultiblocks()
	{
		IPatchouliAPI patAPI = PatchouliAPI.get();
		BloodMagicAPI bmAPI = BloodMagicAPI.INSTANCE;

		// Rituals
		List<Ritual> rituals = BloodMagic.RITUAL_MANAGER.getSortedRituals();
		for (Ritual ritual : rituals)
		{
			Map<BlockPos, EnumRuneType> ritualMap = Maps.newHashMap();
			List<RitualComponent> components = Lists.newArrayList();
			ritual.gatherComponents(components::add);

			resetMinMaxValues(); // Clear previous Scale
			for (RitualComponent component : components) // Set Scale and Map
			{
				ritualMap.put(component.getOffset(), component.getRuneType());
				checkAndSetMinMaxValues(component.getX(Direction.NORTH), component.getY(), component.getZ(Direction.NORTH));
			}

			String[][] pattern = makePattern(ritualMap, Collections.emptyMap());

			// @formatter:off
			IMultiblock multiblock = patAPI.makeMultiblock(
					pattern,
					'B', BloodMagicBlocks.BLANK_RITUAL_STONE.get(),
					'W', BloodMagicBlocks.WATER_RITUAL_STONE.get(),
					'F', BloodMagicBlocks.FIRE_RITUAL_STONE.get(),
					'E', BloodMagicBlocks.EARTH_RITUAL_STONE.get(),
					'A', BloodMagicBlocks.AIR_RITUAL_STONE.get(),
					'D', BloodMagicBlocks.DUSK_RITUAL_STONE.get(),
					'd', BloodMagicBlocks.DAWN_RITUAL_STONE.get(),
					'0', BloodMagicBlocks.MASTER_RITUAL_STONE.get()
			); 
			// @formatter:on

			patAPI.registerMultiblock(new ResourceLocation(BloodMagic.MODID, BloodMagic.RITUAL_MANAGER.getId(ritual)), multiblock);
		}

		// Blood Altars
		for (AltarTier tier : AltarTier.values())
		{
			String[][] pattern;
			int shiftMultiblock = 1; // Most Blood Altar Renders should overlap an existing Blood Altar.

			if (tier.equals(AltarTier.ONE)) // Special Case Tier 1 (just the Altar)
			{
				pattern = new String[][] { { "0" }, { "_" } }; // Second level is for multiblock render scale.
				shiftMultiblock = 0; // This one should not overlap an existing Blood Altar.
			} else if (tier.equals(AltarTier.TWO)) // Special Case Tier 2. Non-upgraded Runes in corners.
			{
				pattern = new String[][] { { "___", "_0_", "___" }, { "rRr", "R_R", "rRr" } };
			} else
			{
				Map<BlockPos, ComponentType> altarMap = Maps.newHashMap();
				List<AltarComponent> components = tier.getAltarComponents();

				resetMinMaxValues(); // Clear previous Scale
				for (AltarComponent component : components) // Set Scale and Map
				{
					BlockPos offset = component.getOffset();
					altarMap.put(offset, component.getComponent());
					checkAndSetMinMaxValues(offset.getX(), offset.getY(), offset.getZ());
				}
				pattern = makePattern(Collections.emptyMap(), altarMap);
			}

			BMStateMatcher bloodRuneSM = new BMStateMatcher(bmAPI.getComponentStates(ComponentType.BLOODRUNE));
			BMStateMatcher blankBloodRuneSM = new BMStateMatcher(BloodMagicBlocks.BLANK_RUNE.get(), bmAPI.getComponentStates(ComponentType.BLOODRUNE));
			IStateMatcher notAirSM = patAPI.predicateMatcher(Blocks.STONE_BRICKS, state -> state.getMaterial() != Material.AIR && !state.getMaterial().isLiquid());
			BMStateMatcher glowstoneSM = new BMStateMatcher(bmAPI.getComponentStates(ComponentType.GLOWSTONE));
			BMStateMatcher bloodStoneSM = new BMStateMatcher(bmAPI.getComponentStates(ComponentType.BLOODSTONE));
			BMStateMatcher beaconSM = new BMStateMatcher(bmAPI.getComponentStates(ComponentType.BEACON));
			BMStateMatcher crystalSM = new BMStateMatcher(bmAPI.getComponentStates(ComponentType.CRYSTAL));

			// @formatter:off
			IMultiblock multiblock = patAPI.makeMultiblock(
					pattern,
					'R', bloodRuneSM,
					'P', notAirSM,
					'G', glowstoneSM,
					'S', bloodStoneSM,
					'B', beaconSM,
					'C', crystalSM,
					'r', blankBloodRuneSM,
					'0', BloodMagicBlocks.BLOOD_ALTAR.get()
			);
			// @formatter:on
			multiblock.offset(0, shiftMultiblock, 0);

			patAPI.registerMultiblock(new ResourceLocation(BloodMagic.MODID, "altar_" + tier.name().toLowerCase()), multiblock);
		}
	}

	private void resetMinMaxValues()
	{
		maxX = minX = maxY = minY = maxZ = minZ = 0;
	}

	private void checkAndSetMinMaxValues(int x, int y, int z)
	{
		if (x > maxX)
		{
			maxX = x;
		}
		if (x < minX)
		{
			minX = x;
		}
		if (y > maxY)
		{
			maxY = y;
		}
		if (y < minY)
		{
			minY = y;
		}
		if (z > maxZ)
		{
			maxZ = z;
		}
		if (z < minZ)
		{
			minZ = z;
		}
	}

	private String[][] makePattern(Map<BlockPos, EnumRuneType> ritualMap, Map<BlockPos, ComponentType> altarMap)
	{
		String[][] pattern = new String[1 + maxY - minY][1 + maxX - minX];
		for (int y = maxY; y >= minY; y--) // Top to Bottom
		{
			for (int x = minX; x <= maxX; x++) // West to East
			{
				StringBuilder row = new StringBuilder();
				for (int z = minZ; z <= maxZ; z++) // North to South
				{
					BlockPos pos = new BlockPos(x, y, z);

					if (!ritualMap.isEmpty()) // Rituals
					{
						EnumRuneType rune = ritualMap.get(pos);
						if (rune != null)
						{
							String name = rune.name();
							PickRune: switch (name)
							{
							case "BLANK":
								row.append('B');
								break PickRune;
							case "WATER":
								row.append('W');
								break PickRune;
							case "FIRE":
								row.append('F');
								break PickRune;
							case "EARTH":
								row.append('E');
								break PickRune;
							case "AIR":
								row.append('A');
								break PickRune;
							case "DUSK":
								row.append('D');
								break PickRune;
							case "DAWN":
								row.append('d');
								break PickRune;
							}
						} else
						{
							row.append(checkEmptySpace(x, y, z));
						}
					} else if (!altarMap.isEmpty()) // Blood Altars
					{
						ComponentType component = altarMap.get(pos);
						if (component != null)
						{
							String name = component.name();
							PickComponent: switch (name)
							{
							case "BLOODRUNE":
								row.append('R');
								break PickComponent;
							case "NOTAIR":
								row.append('P');
								break PickComponent;
							case "GLOWSTONE":
								row.append('G');
								break PickComponent;
							case "BLOODSTONE":
								row.append('S');
								break PickComponent;
							case "BEACON":
								row.append('B');
								break PickComponent;
							case "CRYSTAL":
								row.append('C');
								break PickComponent;
							}
						} else
						{
							row.append(checkEmptySpace(x, y, z));
						}
					}
				}
				pattern[maxY - y][x - minX] = row.toString();
			}
		}
		return pattern;
	}

	private Character checkEmptySpace(int x, int y, int z)
	{
		if (x == 0 && y == 0 && z == 0)
		{
			return '0'; // Center of Multiblock (MRS or Blood Altar)
		} else
			return '_'; // Patchouli's "Any Block" Symbol
	}

	private class BMStateMatcher implements IStateMatcher
	{
		private final List<BlockState> render; // BlockStates to Render
		private final List<BlockState> valid; // BlockStates that are Valid.

		private BMStateMatcher(Block singleBlockRender, List<BlockState> valid)
		{
			List<BlockState> render = Lists.newArrayList();
			render.add(singleBlockRender.getDefaultState());
			this.render = render;
			this.valid = valid;
		}

		private BMStateMatcher(List<BlockState> renderAndValid)
		{
			this.render = renderAndValid;
			this.valid = renderAndValid;
		}

		@Override
		public BlockState getDisplayedState(int ticks)
		{
			if (render.isEmpty())
			{
				return Blocks.BEDROCK.getDefaultState(); // show something impossible
			} else
			{
				int idx = (ticks / 20) % render.size();
				return render.get(idx);
			}
		}

		@Override
		public TriPredicate<IBlockReader, BlockPos, BlockState> getStatePredicate()
		{
			return (w, p, s) -> valid.contains(s);
		}
	}
}
