package wayoftime.bloodmagic.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BloodMagicPotions
{
	public static final Effect SOUL_SNARE = new PotionSoulSnare();
	public static final Effect FIRE_FUSE = new PotionFireFuse();
	public static final Effect SOUL_FRAY = new PotionBloodMagic(EffectType.HARMFUL, 0xFFFFFFFF);
	public static final Effect PLANT_LEECH = new PotionPlantLeech();
	public static final Effect SACRIFICIAL_LAMB = new PotionSacrificialLamb();
	public static final Effect FLIGHT = new PotionBloodMagic(EffectType.BENEFICIAL, 0x23DDE1);
	public static final Effect SPECTRAL_SIGHT = new PotionBloodMagic(EffectType.BENEFICIAL, 0x2FB813);
	public static final Effect GRAVITY = new PotionBloodMagic(EffectType.HARMFUL, 0x800080);
	public static final Effect HEAVY_HEART = new PotionHeavyHeart();
	public static final Effect GROUNDED = new PotionBloodMagic(EffectType.HARMFUL, 0xBA855B);
	public static final Effect SUSPENDED = new PotionSuspended();
	public static final Effect PASSIVITY = new PotionPassivity();
	public static final Effect BOUNCE = new PotionBloodMagic(EffectType.BENEFICIAL, 0x57FF2E);

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
		reg.register(GRAVITY.addAttributesModifier(ForgeMod.ENTITY_GRAVITY.get(), "AF8B6E3F-3328-4C0A-AA66-6BA6BB6DBEF6", (double) 0.5F, AttributeModifier.Operation.MULTIPLY_BASE).setRegistryName("gravity"));
		reg.register(HEAVY_HEART.setRegistryName("heavy_heart"));
		reg.register(GROUNDED.setRegistryName("grounded"));
		reg.register(SUSPENDED.setRegistryName("suspended"));
		reg.register(PASSIVITY.setRegistryName("passivity"));
		reg.register(BOUNCE.setRegistryName("bounce"));
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
