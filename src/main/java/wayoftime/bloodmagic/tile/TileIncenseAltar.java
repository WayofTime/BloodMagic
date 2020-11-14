package wayoftime.bloodmagic.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IIncensePath;
import wayoftime.bloodmagic.incense.IncenseAltarHandler;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class TileIncenseAltar extends TileInventory implements ITickableTileEntity
{
	public static int maxCheckRange = 5;
	public AreaDescriptor incenseArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
	public Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<>();

	public double incenseAddition = 0; // Self-sacrifice is multiplied by 1 plus this value.
	public double tranquility = 0;
	public int roadDistance = 0; // Number of road blocks laid down

	@ObjectHolder("bloodmagic:incensealtar")
	public static TileEntityType<TileIncenseAltar> TYPE;

	public TileIncenseAltar(TileEntityType<?> type)
	{
		super(type, 1, "incensealtar");
	}

	public TileIncenseAltar()
	{
		this(TYPE);
	}

	@Override
	public void tick()
	{
		AxisAlignedBB aabb = incenseArea.getAABB(getPos());
		List<PlayerEntity> playerList = getWorld().getEntitiesWithinAABB(PlayerEntity.class, aabb);
		if (playerList.isEmpty())
		{
			return;
		}

		if (getWorld().getGameTime() % 100 == 0)
		{
			recheckConstruction();
		}

		boolean hasPerformed = false;

		for (PlayerEntity player : playerList)
		{
			if (PlayerSacrificeHelper.incrementIncense(player, 0, incenseAddition, incenseAddition / 100))
			{
				hasPerformed = true;
			}
		}

		if (hasPerformed)
		{
			if (getWorld().rand.nextInt(4) == 0 && getWorld() instanceof ServerWorld)
			{
				ServerWorld server = (ServerWorld) getWorld();
				server.spawnParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.02, 0.03, 0.02, 0);
			}
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);
		tranquility = tag.getDouble("tranquility");
		incenseAddition = tag.getDouble("incenseAddition");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
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
				BlockPos verticalPos = pos.add(0, i, 0);

				canFormRoad = true;
				level: for (int index = 0; index < 4; index++)
				{
					Direction horizontalFacing = Direction.byHorizontalIndex(index);
					BlockPos facingOffsetPos = verticalPos.offset(horizontalFacing, currentDistance);
					for (int j = -1; j <= 1; j++)
					{
						BlockPos offsetPos = facingOffsetPos.offset(horizontalFacing.rotateY(), j);
						BlockState state = getWorld().getBlockState(offsetPos);
						Block block = state.getBlock();
						if (!(block instanceof IIncensePath && ((IIncensePath) block).getLevelOfPath(getWorld(), offsetPos, state) >= currentDistance - 2))
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
							BlockPos offsetPos = pos.add(i, y, j);
							BlockState state = getWorld().getBlockState(offsetPos);
							Block block = state.getBlock();
							TranquilityStack stack = IncenseTranquilityRegistry.getTranquilityOfBlock(getWorld(), offsetPos, block, state);
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

		double bonus = IncenseAltarHandler.getIncenseBonusFromComponents(getWorld(), pos, appliedTranquility, roadDistance);
		incenseAddition = bonus;
		this.tranquility = appliedTranquility;
	}
}
