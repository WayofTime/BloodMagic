package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall;
import WayofTime.bloodmagic.ritual.harvest.HarvestRegistry;
import net.minecraft.init.Blocks;

public class ModRituals
{
    // TODO Move elsewhere
    public static void initHarvestHandlers()
    {
        HarvestRegistry.registerRangeAmplifier(Blocks.DIAMOND_BLOCK.getDefaultState(), 15);
        HarvestRegistry.registerRangeAmplifier(Blocks.GOLD_BLOCK.getDefaultState(), 10);
        HarvestRegistry.registerRangeAmplifier(Blocks.IRON_BLOCK.getDefaultState(), 6);

        HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
        HarvestRegistry.registerHandler(new HarvestHandlerTall());
        HarvestRegistry.registerHandler(new HarvestHandlerStem());
    }
}
