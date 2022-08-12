package wayoftime.bloodmagic.client.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundRegisterListener
{
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(SoundRegistrator.BLEEDING_EDGE_MUSIC);
	}
}
