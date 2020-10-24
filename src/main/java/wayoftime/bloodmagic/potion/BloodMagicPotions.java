package wayoftime.bloodmagic.potion;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BloodMagicPotions
{
	public static final Effect soulSnare = new PotionSoulSnare();

	public static void registerPotions(RegistryEvent.Register<Effect> evt)
	{
		IForgeRegistry<Effect> reg = evt.getRegistry();
		reg.register(soulSnare.setRegistryName("soulsnare"));
	}
}
