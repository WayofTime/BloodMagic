package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.AxisAlignedBB;
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

public class RitualAnimalGrowth extends Ritual
{
    public static final String GROWTH_RANGE = "growing";

    public RitualAnimalGrowth()
    {
        super("ritualAnimalGrowth", 0, 10000, "ritual." + Constants.Mod.MODID + ".animalGrowthRitual");
        addBlockRange(GROWTH_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 1, -2), 5, 2, 5));
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

        int maxGrowths = currentEssence / getRefreshCost();
        int totalGrowths = 0;

        AreaDescriptor growingRange = getBlockRange(GROWTH_RANGE);
        AxisAlignedBB axis = growingRange.getAABB(masterRitualStone.getBlockPos());
        List<EntityAgeable> animalList = world.getEntitiesWithinAABB(EntityAgeable.class, axis);

        for (EntityAgeable entity : animalList)
        {
            if (entity.getGrowingAge() < 0)
            {
                entity.addGrowth(5);
                totalGrowths++;
            }

            if (totalGrowths >= maxGrowths)
            {
                break;
            }
        }

        network.syphon(totalGrowths * getRefreshCost());
    }

    @Override
    public int getRefreshTime()
    {
        return 20;
    }

    @Override
    public int getRefreshCost()
    {
        return 2;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 2, 0, EnumRuneType.DUSK);
        this.addParallelRunes(components, 1, 0, EnumRuneType.WATER);
        components.add(new RitualComponent(new BlockPos(1, 0, 2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(1, 0, -2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-1, 0, 2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(-1, 0, -2), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(2, 0, 1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(2, 0, -1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-2, 0, 1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-2, 0, -1), EnumRuneType.AIR));

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualAnimalGrowth();
    }
}
