package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileDemonCrystal;
import wayoftime.bloodmagic.ritual.*;

import java.util.function.Consumer;

@RitualRegister("crystal_split")
public class RitualCrystalSplit extends Ritual
{
	public RitualCrystalSplit()
	{
		super("ritualCrystalSplit", 0, 20000, "ritual." + BloodMagic.MODID + ".crystalSplitRitual");
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();
		Direction direction = masterRitualStone.getDirection();
		BlockPos rawPos = pos.above(2);

		BlockEntity tile = world.getBlockEntity(rawPos);
		if (!(tile instanceof TileDemonCrystal) || ((TileDemonCrystal) tile).getWillType() != EnumDemonWillType.DEFAULT)
		{
			return;
		}

		BlockState rawState = world.getBlockState(rawPos);

		TileDemonCrystal rawTile = (TileDemonCrystal) tile;
		if (rawTile.getCrystalCount() >= 5)
		{
			BlockPos vengefulPos = pos.relative(rotateFacing(Direction.NORTH, direction)).above();
			BlockPos corrosivePos = pos.relative(rotateFacing(Direction.EAST, direction)).above();
			BlockPos steadfastPos = pos.relative(rotateFacing(Direction.SOUTH, direction)).above();
			BlockPos destructivePos = pos.relative(rotateFacing(Direction.WEST, direction)).above();

			int vengefulCrystals = 0;
			int corrosiveCrystals = 0;
			int steadfastCrystals = 0;
			int destructiveCrystals = 0;

			tile = world.getBlockEntity(vengefulPos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.VENGEFUL && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				vengefulCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isEmptyBlock(vengefulPos))
			{
				// #donothing, no point setting the crystal to 0 again
			} else
			{
				return;
			}

			tile = world.getBlockEntity(corrosivePos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.CORROSIVE && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				corrosiveCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isEmptyBlock(corrosivePos))
			{

			} else
			{
				return;
			}

			tile = world.getBlockEntity(steadfastPos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.STEADFAST && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				steadfastCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isEmptyBlock(steadfastPos))
			{

			} else
			{
				return;
			}

			tile = world.getBlockEntity(destructivePos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.DESTRUCTIVE && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				destructiveCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isEmptyBlock(destructivePos))
			{

			} else
			{
				return;
			}

			rawTile.setCrystalCount(rawTile.getCrystalCount() - 4);

			growCrystal(world, vengefulPos, EnumDemonWillType.VENGEFUL, vengefulCrystals);
			growCrystal(world, corrosivePos, EnumDemonWillType.CORROSIVE, corrosiveCrystals);
			growCrystal(world, steadfastPos, EnumDemonWillType.STEADFAST, steadfastCrystals);
			growCrystal(world, destructivePos, EnumDemonWillType.DESTRUCTIVE, destructiveCrystals);
			rawTile.setChanged();
			world.sendBlockUpdated(rawPos, rawState, rawState, 3);
		}
	}

	public Direction rotateFacing(Direction facing, Direction rotation)
	{
		switch (rotation)
		{
		case EAST:
			return facing.getClockWise();
		case SOUTH:
			return facing.getClockWise().getClockWise();
		case WEST:
			return facing.getCounterClockWise();
		case NORTH:
		default:
			return facing;
		}
	}

	public void growCrystal(Level world, BlockPos pos, EnumDemonWillType type, int currentCrystalCount)
	{
		if (currentCrystalCount <= 0)
		{
			BlockState state;
			switch (type)
			{
			case CORROSIVE:
				state = BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get().defaultBlockState();
				break;
			case DEFAULT:
				state = BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get().defaultBlockState();
				break;
			case DESTRUCTIVE:
				state = BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get().defaultBlockState();
				break;
			case STEADFAST:
				state = BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get().defaultBlockState();
				break;
			case VENGEFUL:
				state = BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get().defaultBlockState();
				break;
			default:
				state = BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get().defaultBlockState();
			}
			world.setBlock(pos, state, 3);
		} else
		{
			TileDemonCrystal tile = (TileDemonCrystal) world.getBlockEntity(pos);
			tile.setCrystalCount(currentCrystalCount + 1);
			tile.setChanged();
			BlockState state = world.getBlockState(pos);
			world.sendBlockUpdated(pos, state, state, 3);
		}
	}

	@Override
	public int getRefreshTime()
	{
		return 20;
	}

	@Override
	public int getRefreshCost()
	{
		return 1000;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 0, 0, -1, EnumRuneType.FIRE);
		addRune(components, 1, 0, 0, EnumRuneType.EARTH);
		addRune(components, 0, 0, 1, EnumRuneType.WATER);
		addRune(components, -1, 0, 0, EnumRuneType.AIR);

		this.addOffsetRunes(components, 1, 2, -1, EnumRuneType.DUSK);
		this.addCornerRunes(components, 1, 0, EnumRuneType.BLANK);
		this.addParallelRunes(components, 2, 0, EnumRuneType.DUSK);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualCrystalSplit();
	}

	@Override
	public Component[] provideInformationOfRitualToPlayer(Player player)
	{
		return new Component[] { Component.translatable(this.getTranslationKey() + ".info") };
	}
}
