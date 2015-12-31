package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import WayofTime.bloodmagic.api.ritual.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualWater extends Ritual
{
    public static final String WATER_RANGE = "waterRange";

    public RitualWater()
    {
        super("ritualWater", 0, 500, "ritual." + Constants.Mod.MODID + ".waterRitual");
        addBlockRange(WATER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), new BlockPos(0, 1, 0)));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorld();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner(), world);
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
            return;

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;
        
        AreaDescriptor waterRange = getBlockRange(WATER_RANGE);

        for (BlockPos newPos : waterRange.getContainedPositions(masterRitualStone.getPos()))
        {
            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, Blocks.water.getDefaultState());
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
        return 25;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.WATER);

        return components;
    }
    
    @Override
    public Ritual getNewCopy()
    {
        return new RitualWater();
    }
}
