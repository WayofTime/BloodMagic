package wayoftime.bloodmagic.potion;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BloodMagicPotions
{
	public static final MobEffect SOUL_SNARE = new PotionSoulSnare();
	public static final MobEffect FIRE_FUSE = new PotionFireFuse();
	public static final MobEffect SOUL_FRAY = new PotionBloodMagic(MobEffectCategory.HARMFUL, 0xFFFFFFFF);
	public static final MobEffect PLANT_LEECH = new PotionPlantLeech();
	public static final MobEffect SACRIFICIAL_LAMB = new PotionSacrificialLamb();
	public static final MobEffect FLIGHT = new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x23DDE1);
	public static final MobEffect SPECTRAL_SIGHT = new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x2FB813);
	public static final MobEffect GRAVITY = new PotionBloodMagic(MobEffectCategory.HARMFUL, 0x800080);
	public static final MobEffect HEAVY_HEART = new PotionHeavyHeart();
	public static final MobEffect GROUNDED = new PotionBloodMagic(MobEffectCategory.HARMFUL, 0xBA855B);
	public static final MobEffect SUSPENDED = new PotionSuspended();
	public static final MobEffect PASSIVITY = new PotionPassivity();
	public static final MobEffect BOUNCE = new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x57FF2E);
	public static final MobEffect OBSIDIAN_CLOAK = new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x3C1A8D);
	public static final MobEffect HARD_CLOAK = new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x3C1A8D);
	public static final MobEffect SOFT_FALL = new PotionSoftFall();

	public static void registerPotions(RegistryEvent.Register<MobEffect> evt)
	{
		IForgeRegistry<MobEffect> reg = evt.getRegistry();
		reg.register(SOUL_SNARE.setRegistryName("soulsnare"));
		reg.register(FIRE_FUSE.setRegistryName("firefuse"));
		reg.register(SOUL_FRAY.setRegistryName("soulfray"));
		reg.register(PLANT_LEECH.setRegistryName("plantleech"));
		reg.register(SACRIFICIAL_LAMB.setRegistryName("sacrificiallamb"));
		reg.register(FLIGHT.setRegistryName("flight"));
		reg.register(SPECTRAL_SIGHT.setRegistryName("spectral_sight"));
		reg.register(GRAVITY.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "AF8B6E3F-3328-4C0A-AA66-6BA6BB6DBEF6", (double) 0.5F, AttributeModifier.Operation.MULTIPLY_BASE).setRegistryName("gravity"));
		reg.register(HEAVY_HEART.setRegistryName("heavy_heart"));
		reg.register(GROUNDED.setRegistryName("grounded"));
		reg.register(SUSPENDED.setRegistryName("suspended"));
		reg.register(PASSIVITY.setRegistryName("passivity"));
		reg.register(BOUNCE.setRegistryName("bounce"));
		reg.register(OBSIDIAN_CLOAK.setRegistryName("obsidian_cloak"));
		reg.register(HARD_CLOAK.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "BF8B6E3F-3328-4C0A-AA66-3BA6BB6DBEF6", 3, AttributeModifier.Operation.ADDITION).setRegistryName("hard_cloak"));
		reg.register(SOFT_FALL.setRegistryName("soft_fall"));

	}

	public static MobEffect getEffect(ResourceLocation rl)
	{
		return ForgeRegistries.POTIONS.getValue(rl);
	}

	public static ResourceLocation getRegistryName(MobEffect effect)
	{
		return ForgeRegistries.POTIONS.getKey(effect);
	}
}
