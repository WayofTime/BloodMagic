package WayofTime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.TileDemonCrystal;

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
        EnumFacing direction = masterRitualStone.getDirection();
        BlockPos rawPos = pos.up(2);

        TileEntity tile = world.getTileEntity(rawPos);
        if (!(tile instanceof TileDemonCrystal) || ((TileDemonCrystal) tile).getType() != EnumDemonWillType.DEFAULT)
        {
            return;
        }

        IBlockState rawState = world.getBlockState(rawPos);

        TileDemonCrystal rawTile = (TileDemonCrystal) tile;
        if (rawTile.crystalCount >= 5)
        {
            BlockPos vengefulPos = pos.offset(rotateFacing(EnumFacing.NORTH, direction)).up();
            BlockPos corrosivePos = pos.offset(rotateFacing(EnumFacing.EAST, direction)).up();
            BlockPos steadfastPos = pos.offset(rotateFacing(EnumFacing.SOUTH, direction)).up();
            BlockPos destructivePos = pos.offset(rotateFacing(EnumFacing.WEST, direction)).up();

            int vengefulCrystals = 0;
            int corrosiveCrystals = 0;
            int steadfastCrystals = 0;
            int destructiveCrystals = 0;

            tile = world.getTileEntity(vengefulPos);
            if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getType() == EnumDemonWillType.VENGEFUL && ((TileDemonCrystal) tile).crystalCount < 7)
            {
                vengefulCrystals = ((TileDemonCrystal) tile).crystalCount;
            } else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(vengefulPos))
            {
                // #donothing, no point setting the crystal to 0 again
            } else
            {
                return;
            }

            tile = world.getTileEntity(corrosivePos);
            if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getType() == EnumDemonWillType.CORROSIVE && ((TileDemonCrystal) tile).crystalCount < 7)
            {
                corrosiveCrystals = ((TileDemonCrystal) tile).crystalCount;
            } else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(corrosivePos))
            {

            } else
            {
                return;
            }

            tile = world.getTileEntity(steadfastPos);
            if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getType() == EnumDemonWillType.STEADFAST && ((TileDemonCrystal) tile).crystalCount < 7)
            {
                steadfastCrystals = ((TileDemonCrystal) tile).crystalCount;
            } else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(steadfastPos))
            {

            } else
            {
                return;
            }

            tile = world.getTileEntity(destructivePos);
            if (tile instanceof TileDemonCrystal && ((TileDemonCrystal) tile).getType() == EnumDemonWillType.DESTRUCTIVE && ((TileDemonCrystal) tile).crystalCount < 7)
            {
                destructiveCrystals = ((TileDemonCrystal) tile).crystalCount;
            } else if (!(tile instanceof TileDemonCrystal) && world.isAirBlock(destructivePos))
            {

            } else
            {
                return;
            }

            rawTile.crystalCount -= 4;

            growCrystal(world, vengefulPos, EnumDemonWillType.VENGEFUL, vengefulCrystals);
            growCrystal(world, corrosivePos, EnumDemonWillType.CORROSIVE, corrosiveCrystals);
            growCrystal(world, steadfastPos, EnumDemonWillType.STEADFAST, steadfastCrystals);
            growCrystal(world, destructivePos, EnumDemonWillType.DESTRUCTIVE, destructiveCrystals);
            rawTile.markDirty();
            world.notifyBlockUpdate(rawPos, rawState, rawState, 3);
        }
    }

    public EnumFacing rotateFacing(EnumFacing facing, EnumFacing rotation)
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
            world.setBlockState(pos, RegistrarBloodMagicBlocks.DEMON_CRYSTAL.getStateFromMeta(type.ordinal()), 3);
        } else
        {
            TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
            tile.crystalCount++;
            tile.markDirty();
            IBlockState state = world.getBlockState(pos);
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
    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player)
    {
        return new ITextComponent[] { new TextComponentTranslation(this.getUnlocalizedName() + ".info") };
    }
}
