package wayoftime.bloodmagic.client.sounds;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class SoundRegisterListener
{
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void registerSoundEvents(RegisterEvent event)
	{
		if (event.getRegistryKey() == ForgeRegistries.Keys.SOUND_EVENTS ) {
			event.register(ForgeRegistries.Keys.SOUND_EVENTS ,SoundRegistrator.BLEEDING_EDGE_MUSIC.getLocation(), () -> SoundRegistrator.BLEEDING_EDGE_MUSIC);
		}
	}
}
