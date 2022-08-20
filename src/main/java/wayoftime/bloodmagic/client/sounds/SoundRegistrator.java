package wayoftime.bloodmagic.client.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import wayoftime.bloodmagic.BloodMagic;

public class SoundRegistrator
{
	public static final SoundEvent BLEEDING_EDGE_MUSIC;
	static
	{
		BLEEDING_EDGE_MUSIC = addSoundsToRegistry("bleedingedge");
	}

	private static SoundEvent addSoundsToRegistry(String soundId)
	{
		ResourceLocation shotSoundLocation = BloodMagic.rl(soundId);
		SoundEvent soundEvent = new SoundEvent(shotSoundLocation);
		soundEvent.setRegistryName(shotSoundLocation);
		return soundEvent;
	}
}
