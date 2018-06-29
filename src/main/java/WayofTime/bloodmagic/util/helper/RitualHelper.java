package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.ritual.IRitualStone;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

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
        else if (tile != null && tile.hasCapability(RUNE_CAPABILITY, null))
            return true;

        return false;
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
}
