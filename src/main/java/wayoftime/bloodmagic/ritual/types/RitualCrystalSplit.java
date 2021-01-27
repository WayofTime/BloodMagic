package wayoftime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.tile.TileDemonCrystal;

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
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getBlockPos();
		Direction direction = masterRitualStone.getDirection();
		BlockPos rawPos = pos.up(2);

		TileEntity tile = world.getTileEntity(rawPos);
		if (!(tile instanceof TileDemonCrystal) || ((TileDemonCrystal) tile).getWillType() != EnumDemonWillType.DEFAULT)
		{
			return;
		}

		BlockState rawState = world.getBlockState(rawPos);

		TileDemonCrystal rawTile = (TileDemonCrystal) tile;
		if (rawTile.getCrystalCount() >= 5)
		{
			BlockPos vengefulPos = pos.offset(rotateFacing(Direction.NORTH, direction)).up();
			BlockPos corrosivePos = pos.offset(rotateFacing(Direction.EAST, direction)).up();
			BlockPos steadfastPos = pos.offset(rotateFacing(Direction.SOUTH, direction)).up();
			BlockPos destructivePos = pos.offset(rotateFacing(Direction.WEST, direction)).up();

			int vengefulCrystals = 0;
			int corrosiveCrystals = 0;
			int steadfastCrystals = 0;
			int destructiveCrystals = 0;

			tile = world.getTileEntity(vengefulPos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.VENGEFUL && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				vengefulCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(vengefulPos))
			{
				// #donothing, no point setting the crystal to 0 again
			} else
			{
				return;
			}

			tile = world.getTileEntity(corrosivePos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.CORROSIVE && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				corrosiveCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(corrosivePos))
			{

			} else
			{
				return;
			}

			tile = world.getTileEntity(steadfastPos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.STEADFAST && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				steadfastCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(steadfastPos))
			{

			} else
			{
				return;
			}

			tile = world.getTileEntity(destructivePos);
			if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getWillType() == EnumDemonWillType.DESTRUCTIVE && ((TileDemonCrystal) tile).getCrystalCount() < 7)
			{
				destructiveCrystals = ((TileDemonCrystal) tile).getCrystalCount();
			} else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(destructivePos))
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
			rawTile.markDirty();
			world.notifyBlockUpdate(rawPos, rawState, rawState, 3);
		}
	}

	public Direction rotateFacing(Direction facing, Direction rotation)
	{
		switch (rotation)
		{
		case EAST:
			return facing.rotateY();
		case SOUTH:
			return facing.rotateY().rotateY();
		case WEST:
			return facing.rotateYCCW();
		case NORTH:
		default:
			return facing;
		}
	}

	public void growCrystal(World world, BlockPos pos, EnumDemonWillType type, int currentCrystalCount)
	{
		if (currentCrystalCount <= 0)
		{
			BlockState state;
			switch (type)
			{
			case CORROSIVE:
				state = BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get().getDefaultState();
				break;
			case DEFAULT:
				state = BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get().getDefaultState();
				break;
			case DESTRUCTIVE:
				state = BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get().getDefaultState();
				break;
			case STEADFAST:
				state = BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get().getDefaultState();
				break;
			case VENGEFUL:
				state = BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get().getDefaultState();
				break;
			default:
				state = BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get().getDefaultState();
			}
			world.setBlockState(pos, state, 3);
		} else
		{
			TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
			tile.setCrystalCount(currentCrystalCount + 1);
			tile.markDirty();
			BlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
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
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[] { new TranslationTextComponent(this.getTranslationKey() + ".info") };
	}
}
