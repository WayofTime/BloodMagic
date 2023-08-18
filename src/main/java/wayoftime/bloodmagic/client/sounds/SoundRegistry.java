package wayoftime.bloodmagic.client.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;

public class SoundRegistry
{

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BloodMagic.MODID);
	public static final RegistryObject<SoundEvent> BLEEDING_EDGE_MUSIC = SOUNDS.register("bleedingedge" , ()-> addSoundsToRegistry("bleedingedge"));

	private static SoundEvent addSoundsToRegistry(String soundId)
	{
		return SoundEvent.createVariableRangeEvent(BloodMagic.rl(soundId));
	}
}
