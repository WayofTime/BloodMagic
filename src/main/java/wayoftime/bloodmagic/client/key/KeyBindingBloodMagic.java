package wayoftime.bloodmagic.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindingBloodMagic
{
	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		System.out.println("registering keys"); // to debug this
		event.register(KeyBindings.OPEN_HOLDING.getKey());
		event.register(KeyBindings.CYCLE_HOLDING_POS.getKey());
		event.register(KeyBindings.CYCLE_HOLDING_NEG.getKey());
		// registering
	}
}
