package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import WayofTime.bloodmagic.api.ritual.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualLava extends Ritual
{
    public static final String LAVA_RANGE = "lavaRange";

    public RitualLava()
    {
        super("ritualLava", 0, 10000, "ritual." + Constants.Mod.MODID + ".lavaRitual");
        addBlockRange(LAVA_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), new BlockPos(0, 1, 0)));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorld();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner(), world);
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
            return;

        AreaDescriptor lavaRange = getBlockRange(LAVA_RANGE);

        for (BlockPos newPos : lavaRange.getContainedPositions(masterRitualStone.getPos()))
        {
            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, Blocks.lava.getDefaultState());
                network.syphon(getRefreshCost());
            }
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 500;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 1, 0, EnumRuneType.FIRE);

        return components;
    }
}
