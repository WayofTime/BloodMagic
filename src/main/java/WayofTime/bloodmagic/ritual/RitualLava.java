package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualLava extends Ritual
{
    public static final String LAVA_RANGE = "lavaRange";

    public RitualLava()
    {
        super("ritualLava", 0, 10000, "ritual." + Constants.Mod.MODID + ".lavaRitual");
        addBlockRange(LAVA_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor lavaRange = getBlockRange(LAVA_RANGE);

        for (BlockPos newPos : lavaRange.getContainedPositions(masterRitualStone.getBlockPos()))
        {
            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, Blocks.flowing_lava.getDefaultState());
                totalEffects++;
            }

            if (totalEffects >= maxEffects)
            {
                break;
            }
        }

        network.syphon(getRefreshCost() * totalEffects);
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

    @Override
    public Ritual getNewCopy()
    {
        return new RitualLava();
    }
}
