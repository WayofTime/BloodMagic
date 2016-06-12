package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * This ritual uses registered {@link IHarvestHandler}'s to harvest blocks.
 * 
 * To register a new Handler for this ritual use
 * {@link HarvestRegistry#registerHandler(IHarvestHandler)}
 * 
 * This ritual includes a way to change the range based on what block is above
 * the MasterRitualStone. You can use
 * {@link HarvestRegistry#registerRangeAmplifier(BlockStack, int)} to register a
 * new amplifier.
 */
public class RitualHarvest extends Ritual
{
    public static final String HARVEST_RANGE = "harvestRange";

    public RitualHarvest()
    {
        super("ritualHarvest", 0, 20000, "ritual." + Constants.Mod.MODID + ".harvestRitual");
        addBlockRange(HARVEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-4, 1, -4), 9, 5, 9));
        setMaximumVolumeAndDistanceOfRange(HARVEST_RANGE, 81, 15, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        World world = masterRitualStone.getWorldObj();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (network.getCurrentEssence() < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        int harvested = 0;

        AreaDescriptor harvestArea = getBlockRange(HARVEST_RANGE);

        harvestArea.resetIterator();
        while (harvestArea.hasNext())
        {
            BlockPos nextPos = harvestArea.next().add(pos);
            if (harvestBlock(world, nextPos))
            {
                harvested++;
            }
        }

        network.syphon(getRefreshCost() * harvested);
    }

    @Override
    public int getRefreshCost()
    {
        return 20;
    }

    @Override
    public int getRefreshTime()
    {
        return 5;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        components.add(new RitualComponent(new BlockPos(1, 0, 1), EnumRuneType.DUSK));
        components.add(new RitualComponent(new BlockPos(1, 0, -1), EnumRuneType.DUSK));
        components.add(new RitualComponent(new BlockPos(-1, 0, -1), EnumRuneType.DUSK));
        components.add(new RitualComponent(new BlockPos(-1, 0, 1), EnumRuneType.DUSK));
        components.add(new RitualComponent(new BlockPos(2, 0, 0), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-2, 0, 0), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(0, 0, 2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(0, 0, -2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(3, 0, 1), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(3, 0, -1), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-3, 0, 1), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-3, 0, -1), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(1, 0, 3), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-1, 0, 3), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(1, 0, -3), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-1, 0, -3), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(2, 0, 3), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(3, 0, 2), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(2, 0, -3), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(-3, 0, 2), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(-2, 0, 3), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(3, 0, -2), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(-2, 0, -3), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(-3, 0, -2), EnumRuneType.WATER));

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualHarvest();
    }

    public static boolean harvestBlock(World world, BlockPos pos)
    {
        BlockStack harvestStack = BlockStack.getStackFromPos(world, pos);

        for (IHarvestHandler handler : HarvestRegistry.getHandlerList())
            if (handler.harvestAndPlant(world, pos, harvestStack))
                return true;

        return false;
    }
}
