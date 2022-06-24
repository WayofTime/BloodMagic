package wayoftime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("magnetism")
public class RitualMagnetic extends Ritual
{
	public static final String PLACEMENT_RANGE = "placementRange";
	// public static final String SEARCH_RANGE = "searchRange";
	public BlockPos lastPos; // An offset
	private FakePlayer fakePlayer;

	public RitualMagnetic()
	{
		super("ritualMagnetic", 0, 5000, "ritual." + BloodMagic.MODID + ".magneticRitual");
		addBlockRange(PLACEMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3));
		setMaximumVolumeAndDistanceOfRange(PLACEMENT_RANGE, 50, 4, 4);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		Vec3 MRSpos = new Vec3(masterRitualStone.getMasterBlockPos().getX(), masterRitualStone.getMasterBlockPos().getY(), masterRitualStone.getMasterBlockPos().getZ());
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		AreaDescriptor placementRange = masterRitualStone.getBlockRange(PLACEMENT_RANGE);

		BlockPos replacement = pos;
		boolean replace = false;

		for (BlockPos offset : placementRange.getContainedPositions(pos))
		{
			if (world.isEmptyBlock(offset))
			{
				replacement = offset;
				replace = true;
				break;
			}
		}

		BlockState downState = world.getBlockState(pos.below());
		int radius = getRadius(downState.getBlock());

		int maxBlockChecks = 100;
		int checks = 0;

		if (replace)
		{
			int j = -1;
			int i = -radius;
			int k = -radius;

			if (lastPos != null && !lastPos.equals(BlockPos.ZERO))
			{
				j = lastPos.getY();
				i = Math.min(radius, Math.max(-radius, lastPos.getX()));
				k = Math.min(radius, Math.max(-radius, lastPos.getZ()));
			}

			while (j + pos.getY() >= world.getMinBuildHeight())
			{
				while (i <= radius)
				{
					while (k <= radius)
					{
						if (checks >= maxBlockChecks)
						{
							this.lastPos = new BlockPos(i, j, k);
							return;
						}
						checks++;
						BlockPos newPos = pos.offset(i, j, k);
						Vec3 newPosVector = new Vec3(newPos.getX(), newPos.getY(), newPos.getZ());
						BlockState state = world.getBlockState(newPos);
//						RayTraceResult fakeRayTrace = world.rayTraceBlocks(MRSpos, newPosVector, false);
//						ItemStack checkStack = state.getBlock().getPickBlock(state, fakeRayTrace, world, newPos, getFakePlayer((ServerWorld) world));
						ItemStack checkStack = new ItemStack(state.getBlock());
						if (isBlockOre(checkStack))
						{
							Utils.swapLocations(world, newPos, world, replacement);
							masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
							k++;
							this.lastPos = new BlockPos(i, j, k);
							return;
						} else
						{
							k++;
						}
					}
					i++;
					k = -radius;
				}
				j--;
				i = -radius;
			}

			j = -1;
			this.lastPos = new BlockPos(i, j, k);
		}
	}

	public void readFromNBT(CompoundTag tag)
	{
		super.readFromNBT(tag);
		lastPos = new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	public void writeToNBT(CompoundTag tag)
	{
		super.writeToNBT(tag);
		if (lastPos != null)
		{
			tag.putInt(Constants.NBT.X_COORD, lastPos.getX());
			tag.putInt(Constants.NBT.Y_COORD, lastPos.getY());
			tag.putInt(Constants.NBT.Z_COORD, lastPos.getZ());
		}
	}

	public int getRadius(Block block)
	{
		if (block == Blocks.IRON_BLOCK)
		{
			return 7;
		}

		if (block == Blocks.GOLD_BLOCK)
		{
			return 15;
		}

		if (block == Blocks.DIAMOND_BLOCK)
		{
			return 31;
		}

		return 3;
	}

	@Override
	public int getRefreshTime()
	{
		return 40;
	}

	@Override
	public int getRefreshCost()
	{
		return 50;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
		addParallelRunes(components, 2, 1, EnumRuneType.EARTH);
		addCornerRunes(components, 2, 1, EnumRuneType.AIR);
		addParallelRunes(components, 2, 2, EnumRuneType.FIRE);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualMagnetic();
	}

	private FakePlayer getFakePlayer(ServerLevel world)
	{
		return fakePlayer == null
				? fakePlayer = FakePlayerFactory.get(world, new GameProfile(null, BloodMagic.MODID + "_ritual_magnetic"))
				: fakePlayer;
	}

	public static boolean isBlockOre(ItemStack stack)
	{
		if (stack.isEmpty())
			return false;

		return stack.is(Tags.Items.ORES);

//		for(ResourceLocation rl :  stack.getItem().getTags())
//		{
//			rl.getPath()
//		}

//		for (int id : OreDictionary.getOreIDs(stack))
//		{
//			String oreName = OreDictionary.getOreName(id);
//			if (oreName.contains("ore"))
//				return true;
//		}

//		return false;
	}
}
