package WayofTime.bloodmagic.api.util.helper;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;

public class RitualHelper
{
    public static boolean canCrystalActivate(Ritual ritual, int crystalLevel)
    {
        return ritual.getCrystalLevel() <= crystalLevel && RitualRegistry.ritualEnabled(ritual);
    }

    public static String getNextRitualKey(String currentKey)
    {
        int currentIndex = RitualRegistry.getIds().indexOf(currentKey);
        int nextIndex = RitualRegistry.getRituals().listIterator(currentIndex).nextIndex();

        return RitualRegistry.getIds().get(nextIndex);
    }

    public static String getPrevRitualKey(String currentKey)
    {
        int currentIndex = RitualRegistry.getIds().indexOf(currentKey);
        int previousIndex = RitualRegistry.getIds().listIterator(currentIndex).previousIndex();

        return RitualRegistry.getIds().get(previousIndex);
    }

    /**
     * Checks the RitualRegistry to see if the configuration of the ritual
     * stones in the world is valid for the given EnumFacing.
     * 
     * @param world
     *        - The world
     * @param pos
     *        - Location of the MasterRitualStone
     * 
     * @return The ID of the valid ritual
     */
    public static String getValidRitual(World world, BlockPos pos)
    {
        for (String key : RitualRegistry.getIds())
        {
            for (EnumFacing direction : EnumFacing.HORIZONTALS)
            {
                boolean test = checkValidRitual(world, pos, key, direction);
                if (test)
                {
                    return key;
                }
            }
        }

        return "";
    }

    public static EnumFacing getDirectionOfRitual(World world, BlockPos pos, String key)
    {
        for (EnumFacing direction : EnumFacing.HORIZONTALS)
        {
            boolean test = checkValidRitual(world, pos, key, direction);
            if (test)
            {
                return direction;
            }
        }

        return null;
    }

    public static boolean checkValidRitual(World world, BlockPos pos, String ritualId, EnumFacing direction)
    {
        Ritual ritual = RitualRegistry.getRitualForId(ritualId);
        if (ritual == null)
        {
            return false;
        }

        ArrayList<RitualComponent> components = ritual.getComponents();

        if (components == null)
            return false;

        for (RitualComponent component : components)
        {
            BlockPos newPos = pos.add(component.getOffset(direction));
            IBlockState worldState = world.getBlockState(newPos);
            Block block = worldState.getBlock();
            if (block instanceof IRitualStone)
            {
                if (!((IRitualStone) block).isRuneType(world, newPos, component.getRuneType()))
                {
                    return false;
                }
            } else
            {
                return false;
            }
        }

        return true;
    }
}
