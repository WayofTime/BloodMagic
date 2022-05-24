package wayoftime.bloodmagic.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BloodMagicPotions
{
	public static final Effect SOUL_SNARE = new PotionSoulSnare();
	public static final Effect FIRE_FUSE = new PotionFireFuse();
	public static final Effect SOUL_FRAY = new PotionBloodMagic(EffectType.HARMFUL, 0xFFFFFFFF);
	public static final Effect PLANT_LEECH = new PotionBloodMagic(EffectType.HARMFUL, 0x00FF00FF);
	public static final Effect SACRIFICIAL_LAMB = new PotionBloodMagic(EffectType.HARMFUL, 0xFFFFFF);
	public static final Effect FLIGHT = new PotionBloodMagic(EffectType.BENEFICIAL, 0x23DDE1);
	public static final Effect SPECTRAL_SIGHT = new PotionBloodMagic(EffectType.BENEFICIAL, 0x2FB813);

	public static void registerPotions(RegistryEvent.Register<Effect> evt)
	{
		IForgeRegistry<Effect> reg = evt.getRegistry();
		reg.register(SOUL_SNARE.setRegistryName("soulsnare"));
		reg.register(FIRE_FUSE.setRegistryName("firefuse"));
		reg.register(SOUL_FRAY.setRegistryName("soulfray"));
		reg.register(PLANT_LEECH.setRegistryName("plantleech"));
		reg.register(SACRIFICIAL_LAMB.setRegistryName("sacrificiallamb"));
		reg.register(FLIGHT.setRegistryName("flight"));
		reg.register(SPECTRAL_SIGHT.setRegistryName("spectral_sight"));
	}

	public static Effect getEffect(ResourceLocation rl)
	{
		return ForgeRegistries.POTIONS.getValue(rl);
	}

	public static ResourceLocation getRegistryName(Effect effect)
	{
		return ForgeRegistries.POTIONS.getKey(effect);
	}
}
