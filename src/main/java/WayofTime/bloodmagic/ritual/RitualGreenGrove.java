package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualGreenGrove extends Ritual
{
    public static final String GROW_RANGE = "growing";

    public RitualGreenGrove()
    {
        super("ritualGreenGrove", 0, 5000, "ritual." + Constants.Mod.MODID + ".greenGroveRitual");
        addBlockRange(GROW_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 2, -1), 3, 1, 3));
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

        AreaDescriptor growingRange = getBlockRange(GROW_RANGE);

        for (BlockPos newPos : growingRange.getContainedPositions(masterRitualStone.getBlockPos()))
        {
            IBlockState state = world.getBlockState(newPos);
            Block block = state.getBlock();

            if (!BloodMagicAPI.getGreenGroveBlacklist().contains(block))
            {
                if (block instanceof IPlantable || block instanceof IGrowable)
                {
                    block.updateTick(world, newPos, state, new Random());
                    totalGrowths++;
                }
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
        return 5; //TODO: Need to find a way to balance this
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 1, 0, EnumRuneType.WATER);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualGreenGrove();
    }
}
