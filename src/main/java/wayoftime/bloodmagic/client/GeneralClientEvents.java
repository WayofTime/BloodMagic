package wayoftime.bloodmagic.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class GeneralClientEvents
{
	// TODO: Need to fix this so that the Fluid overlay is properly rendered.

//    @SubscribeEvent
//    public static void getFogDensity(EntityViewRenderEvent.FogDensity event) {
//        Camera info = event.getCamera();
//        FluidState fluidState = info.getFluidInCamera();
//        FluidState fluidstate = info.level.getFluidState(info.blockPosition);
//        if (fluidState.isEmpty())
//            return;
//        Fluid fluid = fluidState.getType();
//
//        if (fluid.isSame(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get())) {
//            event.setDensity(1.2f);
//            event.setCanceled(true);
//        }
//
//        if (fluid.isSame(BloodMagicBlocks.DOUBT_FLUID.get())) {
//            event.setDensity(1.2f);
//            event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public static void getFogColor(EntityViewRenderEvent.FogColors event) {
//        Camera info = event.getInfo();
//        FluidState fluidState = info.getFluidInCamera();
//        if (fluidState.isEmpty())
//            return;
//        Fluid fluid = fluidState.getType();
//
//        if (fluid.isSame(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get())) {
//            event.setRed(101 / 256f);
//            event.setGreen(2 / 256f);
//            event.setBlue(6 / 256f);
//        }
//
//        if (fluid.isSame(BloodMagicBlocks.DOUBT_FLUID.get())) {
//            event.setRed(0 / 256f);
//            event.setGreen(91 / 256f);
//            event.setBlue(120 / 256f);
//        }
//    }
}
