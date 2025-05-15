package wayoftime.bloodmagic.client.event;

import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.item.BMItems;

@EventBusSubscriber(modid = BloodMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BMItems.WILL_ITEMS.getEntries().forEach(item -> {
                ItemProperties.register(item.get(), BloodMagic.TYPE_PROPERTY, (stack, level, player, seed) -> stack.getOrDefault(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT).ordinal());
            });
        });
    }
}