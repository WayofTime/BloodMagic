package wayoftime.bloodmagic.common.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wayoftime.bloodmagic.api.compat.IIncensePath;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IncenseAltarHandler;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class TileIncenseAltar extends TileInventory
{
	public static int maxCheckRange = 5;
	public AreaDescriptor incenseArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
	public Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<>();

	public double incenseAddition = 0; // Self-sacrifice is multiplied by 1 plus this value.
	public double tranquility = 0;
	public int roadDistance = 0; // Number of road blocks laid down

	public TileIncenseAltar(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 1, "incensealtar", pos, state);
	}

	public TileIncenseAltar(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.INCENSE_ALTAR_TYPE.get(), pos, state);
	}

	public void tick()
	{
		AABB aabb = incenseArea.getAABB(getBlockPos());
		List<Player> playerList = getLevel().getEntitiesOfClass(Player.class, aabb);
		if (playerList.isEmpty())
		{
			return;
		}

		if (getLevel().getGameTime() % 100 == 0)
		{
			recheckConstruction();
		}

		boolean hasPerformed = false;

		for (Player player : playerList)
		{
			if (PlayerSacrificeHelper.incrementIncense(player, 0, incenseAddition, incenseAddition / 100))
			{
				hasPerformed = true;
			}
		}

		if (hasPerformed)
		{
			if (getLevel().random.nextInt(4) == 0 && getLevel() instanceof ServerLevel)
			{
				ServerLevel server = (ServerLevel) getLevel();
				server.sendParticles(ParticleTypes.FLAME, worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5, 1, 0.02, 0.03, 0.02, 0);
			}
		}
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);
		tranquility = tag.getDouble("tranquility");
		incenseAddition = tag.getDouble("incenseAddition");
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		tag.putDouble("tranquility", tranquility);
		tag.putDouble("incenseAddition", incenseAddition);
		return tag;
	}

	public void recheckConstruction()
	{
		// TODO: Check the physical construction of the incense altar to determine the
		// maximum length.
		int maxLength = 11; // Max length of the path. The path starts two blocks away from the center
							// block.
		int yOffset = 0;

		Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<>();

		for (int currentDistance = 2; currentDistance < currentDistance + maxLength; currentDistance++)
		{
			boolean canFormRoad = false;

			for (int i = -maxCheckRange + yOffset; i <= maxCheckRange + yOffset; i++)
			{
				BlockPos verticalPos = worldPosition.offset(0, i, 0);

				canFormRoad = true;
				level: for (int index = 0; index < 4; index++)
				{
					Direction horizontalFacing = Direction.from2DDataValue(index);
					BlockPos facingOffsetPos = verticalPos.relative(horizontalFacing, currentDistance);
					for (int j = -1; j <= 1; j++)
					{
						BlockPos offsetPos = facingOffsetPos.relative(horizontalFacing.getClockWise(), j);
						BlockState state = getLevel().getBlockState(offsetPos);
						Block block = state.getBlock();
						if (!(block instanceof IIncensePath && ((IIncensePath) block).getLevelOfPath(getLevel(), offsetPos, state) >= currentDistance - 2))
						{
							canFormRoad = false;
							break level;
						}
					}
				}

				if (canFormRoad)
				{
					yOffset = i;
					break;
				}
			}

			if (canFormRoad)
			{
				for (int i = -currentDistance; i <= currentDistance; i++)
				{
					for (int j = -currentDistance; j <= currentDistance; j++)
					{
						if (Math.abs(i) != currentDistance && Math.abs(j) != currentDistance)
						{
							continue; // TODO: Can make this just set j to currentDistance to speed it up.
						}

						for (int y = yOffset; y <= 2 + yOffset; y++)
						{
							BlockPos offsetPos = worldPosition.offset(i, y, j);
							BlockState state = getLevel().getBlockState(offsetPos);
							Block block = state.getBlock();
							TranquilityStack stack = IncenseTranquilityRegistry.getTranquilityOfBlock(getLevel(), offsetPos, block, state);
							if (stack != null)
							{
								if (!tranquilityMap.containsKey(stack.type))
								{
									tranquilityMap.put(stack.type, stack.value);
								} else
								{
									tranquilityMap.put(stack.type, tranquilityMap.get(stack.type) + stack.value);
								}
							}
						}
					}
				}
			} else
			{
				roadDistance = currentDistance - 2;
				break;
			}
		}

		this.tranquilityMap = tranquilityMap;

		double totalTranquility = 0;
		for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
		{
			totalTranquility += entry.getValue();
		}

		if (totalTranquility < 0)
		{
			return;
		}

		double appliedTranquility = 0;
		for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
		{
			appliedTranquility += Math.sqrt(entry.getValue());
		}

		double bonus = IncenseAltarHandler.getIncenseBonusFromComponents(getLevel(), worldPosition, appliedTranquility, roadDistance);
		incenseAddition = bonus;
		this.tranquility = appliedTranquility;
	}
}
