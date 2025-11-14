package wayoftime.bloodmagic.ritual;

import net.minecraft.world.level.block.Blocks;
import wayoftime.bloodmagic.ritual.harvest.*;

public class ModRituals
{
	public static void initHarvestHandlers()
	{
		HarvestRegistry.registerRangeAmplifier(Blocks.DIAMOND_BLOCK.defaultBlockState(), 15);
		HarvestRegistry.registerRangeAmplifier(Blocks.GOLD_BLOCK.defaultBlockState(), 10);
		HarvestRegistry.registerRangeAmplifier(Blocks.IRON_BLOCK.defaultBlockState(), 6);

		HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
		HarvestRegistry.registerHandler(new HarvestHandlerTall());
		HarvestRegistry.registerHandler(new HarvestHandlerStem());
		HarvestRegistry.registerHandler(new HarvestHandlerBerryBush());
		HarvestRegistry.registerHandler(new HarvestHandlerNetherWart());
		HarvestRegistry.registerHandler(new HarvestHandlerGrowingPlant());
		HarvestRegistry.registerHandler(new HarvestHandlerJungleVines());
        HarvestRegistry.registerHandler(new HarvestHandlerAgricraft());
	}
//
//    public static void initCuttingFluids() {
//        CrushingRegistry.registerCuttingFluid(new CrushingHandlerCuttingFluid(ItemCuttingFluid.FluidType.BASIC.getStack(), 250, 0.5));
//        CrushingRegistry.registerCuttingFluid(new CrushingHandlerCuttingFluid(ItemCuttingFluid.FluidType.EXPLOSIVE.getStack(), 25, 0.05));
//    }
}
