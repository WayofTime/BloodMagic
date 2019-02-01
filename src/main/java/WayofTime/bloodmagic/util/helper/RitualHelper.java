package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.ritual.IRitualStone;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class RitualHelper {
    @CapabilityInject(IRitualStone.Tile.class)
    static Capability<IRitualStone.Tile> RUNE_CAPABILITY = null;

    public static boolean canCrystalActivate(Ritual ritual, int crystalLevel) {
        return ritual.getCrystalLevel() <= crystalLevel && BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(ritual), false);
    }

    /**
     * Checks the RitualRegistry to see if the configuration of the ritual
     * stones in the world is valid for the given EnumFacing.
     *
     * @param world - The world
     * @param pos   - Location of the MasterRitualStone
     * @return The ID of the valid ritual
     */
    public static String getValidRitual(World world, BlockPos pos) {
        for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals()) {
            for (EnumFacing direction : EnumFacing.HORIZONTALS) {
                if (checkValidRitual(world, pos, ritual, direction))
                    return BloodMagic.RITUAL_MANAGER.getId(ritual);
            }
        }

        return "";
    }

    public static EnumFacing getDirectionOfRitual(World world, BlockPos pos, Ritual ritual) {
        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            if (checkValidRitual(world, pos, ritual, direction))
                return direction;
        }

        return null;
    }

    public static boolean checkValidRitual(World world, BlockPos pos, Ritual ritual, EnumFacing direction) {
        if (ritual == null) {
            return false;
        }

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);

        for (RitualComponent component : components) {
            BlockPos newPos = pos.add(component.getOffset(direction));
            if (!isRuneType(world, newPos, component.getRuneType()))
                return false;
        }

        return true;
    }

    public static boolean isRuneType(World world, BlockPos pos, EnumRuneType type) {
        if (world == null)
            return false;
        Block block = world.getBlockState(pos).getBlock();
        TileEntity tile = world.getTileEntity(pos);

        if (block instanceof IRitualStone)
            return ((IRitualStone) block).isRuneType(world, pos, type);
        else if (tile instanceof IRitualStone.Tile)
            return ((IRitualStone.Tile) tile).isRuneType(type);
        else if (tile != null && tile.hasCapability(RUNE_CAPABILITY, null))
            return tile.getCapability(RUNE_CAPABILITY, null).isRuneType(type);

        return false;
    }

    public static boolean isRune(World world, BlockPos pos) {
        if (world == null)
            return false;
        Block block = world.getBlockState(pos).getBlock();
        TileEntity tile = world.getTileEntity(pos);

        if (block instanceof IRitualStone)
            return true;
        else if (tile instanceof IRitualStone.Tile)
            return true;
        else return tile != null && tile.hasCapability(RUNE_CAPABILITY, null);

    }

    public static void setRuneType(World world, BlockPos pos, EnumRuneType type) {
        if (world == null)
            return;
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        if (state.getBlock() instanceof IRitualStone)
            ((IRitualStone) state.getBlock()).setRuneType(world, pos, type);
        else if (tile instanceof IRitualStone.Tile)
            ((IRitualStone.Tile) tile).setRuneType(type);
        else if (tile != null && tile.hasCapability(RUNE_CAPABILITY, null)) {
            tile.getCapability(RUNE_CAPABILITY, null).setRuneType(type);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public static boolean createRitual(World world, BlockPos pos, EnumFacing direction, Ritual ritual, boolean safe) {

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);

        if (abortConstruction(world, pos, direction, safe, components)) return false;

        IBlockState mrs = RegistrarBloodMagicBlocks.RITUAL_CONTROLLER.getDefaultState();
        world.setBlockState(pos, mrs);

        setRitualStones(direction, world, pos, components);
        return true;
    }

    public static boolean abortConstruction(World world, BlockPos pos, EnumFacing direction, boolean safe, List<RitualComponent> components) {
        //TODO: can be optimized to check only for the first and last component if every ritual has those at the highest and lowest y-level respectivly.
        for (RitualComponent component : components) {
            BlockPos offset = component.getOffset(direction);
            BlockPos newPos = pos.add(offset);
            if (world.isOutsideBuildHeight(newPos) || (safe && !world.isAirBlock(newPos)))
                return true;
        }
        return false;
    }

    public static boolean repairRitualFromRuins(TileMasterRitualStone tile, boolean safe) {
        Ritual ritual = tile.getCurrentRitual();
        EnumFacing direction;
        Pair<Ritual, EnumFacing> pair;
        if (ritual == null) {
            pair = getRitualFromRuins(tile);
            ritual = pair.getKey();
            direction = pair.getValue();
        } else
            direction = tile.getDirection();

        World world = tile.getWorld();
        BlockPos pos = tile.getPos();

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);

        if (abortConstruction(world, pos, direction, safe, components)) return false;

        setRitualStones(direction, world, pos, components);
        return true;
    }

    public static void setRitualStones(EnumFacing direction, World world, BlockPos pos, List<RitualComponent> gatheredComponents) {
        for (RitualComponent component : gatheredComponents) {
            BlockPos offset = component.getOffset(direction);
            BlockPos newPos = pos.add(offset);
            int meta = component.getRuneType().ordinal();
            IBlockState newState = RegistrarBloodMagicBlocks.RITUAL_STONE.getStateFromMeta(meta);
            world.setBlockState(newPos, newState);
        }
    }


    public static Pair<Ritual, EnumFacing> getRitualFromRuins(TileMasterRitualStone tile) {
        BlockPos pos = tile.getPos();
        World world = tile.getWorld();
        Ritual possibleRitual = tile.getCurrentRitual();
        EnumFacing possibleDirection = tile.getDirection();
        int highestCount = 0;

        if (possibleRitual == null || possibleDirection == null)
            for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals()) {
                for (EnumFacing direction : EnumFacing.HORIZONTALS) {
                    List<RitualComponent> components = Lists.newArrayList();
                    ritual.gatherComponents(components::add);
                    int currentCount = 0;

                    for (RitualComponent component : components) {
                        BlockPos newPos = pos.add(component.getOffset(direction));
                        if (isRuneType(world, newPos, component.getRuneType()))
                            currentCount += 1;
                    }
                    if (currentCount > highestCount) {
                        highestCount = currentCount;
                        possibleRitual = ritual;
                        possibleDirection = direction;
                    }

                }

            }
        return Pair.of(possibleRitual, possibleDirection);
    }
}
