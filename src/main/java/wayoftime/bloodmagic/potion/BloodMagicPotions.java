package wayoftime.bloodmagic.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicPotions {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BloodMagic.MODID);

    public static final RegistryObject<MobEffect> SOUL_SNARE = MOB_EFFECTS.register("soulsnare", PotionSoulSnare::new);
    public static final RegistryObject<MobEffect> FIRE_FUSE = MOB_EFFECTS.register("firefuse", PotionFireFuse::new);
    public static final RegistryObject<MobEffect> SOUL_FRAY = MOB_EFFECTS.register("soulfray", () -> new PotionBloodMagic(MobEffectCategory.HARMFUL, 0xFFFFFFFF));
    public static final RegistryObject<MobEffect> PLANT_LEECH = MOB_EFFECTS.register("plantleech", PotionPlantLeech::new);
    public static final RegistryObject<MobEffect> SACRIFICIAL_LAMB = MOB_EFFECTS.register("sacrificallamb", PotionSacrificialLamb::new);
    public static final RegistryObject<MobEffect> FLIGHT = MOB_EFFECTS.register("flight", () -> new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x23DDE1));
    public static final RegistryObject<MobEffect> SPECTRAL_SIGHT = MOB_EFFECTS.register("spectral_sight", () -> new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x2FB813));
    public static final RegistryObject<MobEffect> GRAVITY = MOB_EFFECTS.register("gravity", () -> new PotionBloodMagic(MobEffectCategory.HARMFUL, 0x800080).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "AF8B6E3F-3328-4C0A-AA66-6BA6BB6DBEF6", 0.5F, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> HEAVY_HEART = MOB_EFFECTS.register("heavy_heart", PotionHeavyHeart::new);
    public static final RegistryObject<MobEffect> GROUNDED = MOB_EFFECTS.register("grounded", () -> new PotionBloodMagic(MobEffectCategory.HARMFUL, 0xBA855B));
    public static final RegistryObject<MobEffect> SUSPENDED = MOB_EFFECTS.register("suspended", PotionSuspended::new);
    public static final RegistryObject<MobEffect> PASSIVITY = MOB_EFFECTS.register("passivity", PotionPassivity::new);
    public static final RegistryObject<MobEffect> BOUNCE = MOB_EFFECTS.register("bounce", () -> new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x57FF2E));
    public static final RegistryObject<MobEffect> OBSIDIAN_CLOAK = MOB_EFFECTS.register("obsidian_cloak", () -> new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x3C1A8D));
    public static final RegistryObject<MobEffect> HARD_CLOAK = MOB_EFFECTS.register("hard_cloak", () -> new PotionBloodMagic(MobEffectCategory.BENEFICIAL, 0x3C1A8D).addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "BF8B6E3F-3328-4C0A-AA66-3BA6BB6DBEF6", 3, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> SOFT_FALL = MOB_EFFECTS.register("soft_fall", PotionSoftFall::new);


	public static MobEffect getEffect(ResourceLocation rl)
	{
		return ForgeRegistries.MOB_EFFECTS.getValue(rl);
	}

	public static ResourceLocation getRegistryName(MobEffect effect)
	{
		return ForgeRegistries.MOB_EFFECTS.getKey(effect);
	}
}
