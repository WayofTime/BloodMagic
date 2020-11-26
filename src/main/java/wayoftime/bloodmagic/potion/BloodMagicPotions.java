package wayoftime.bloodmagic.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BloodMagicPotions
{
	public static final Effect SOUL_SNARE = new PotionSoulSnare();
	public static final Effect FIRE_FUSE = new PotionFireFuse();
	public static final Effect SOUL_FRAY = new PotionBloodMagic(EffectType.HARMFUL, 0xFFFFFFFF);
	public static final Effect PLANT_LEECH = new PotionBloodMagic(EffectType.HARMFUL, 0x00FF00FF);
	public static final Effect SACRIFICIAL_LAMB = new PotionBloodMagic(EffectType.HARMFUL, 0xFFFFFF);

	public static void registerPotions(RegistryEvent.Register<Effect> evt)
	{
		IForgeRegistry<Effect> reg = evt.getRegistry();
		reg.register(SOUL_SNARE.setRegistryName("soulsnare"));
		reg.register(FIRE_FUSE.setRegistryName("firefuse"));
		reg.register(SOUL_FRAY.setRegistryName("soulfray"));
		reg.register(PLANT_LEECH.setRegistryName("plantleech"));
		reg.register(SACRIFICIAL_LAMB.setRegistryName("sacrificiallamb"));
	}
}
