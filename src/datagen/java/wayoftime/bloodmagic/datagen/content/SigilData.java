package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidType;
import wayoftime.bloodmagic.api.BMIdentifiers.Sigils;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.sigil.ApplyPotionEffect;
import wayoftime.bloodmagic.common.sigil.DivinationEffect;
import wayoftime.bloodmagic.common.sigil.FluidPlaceEffect;
import wayoftime.bloodmagic.common.sigil.FluidRemoveEffect;

import java.util.Optional;
import java.util.function.BiConsumer;

public class SigilData {
    public static void sigilTypes(BootstrapContext<SigilEffect> context) {
        context.register(Sigils.DIVINATION, new DivinationEffect(false));
        context.register(Sigils.SEER, new DivinationEffect(true));
        context.register(Sigils.LAVA, new FluidPlaceEffect(Fluids.LAVA.builtInRegistryHolder(), 10 * FluidType.BUCKET_VOLUME, 1000));
        context.register(Sigils.WATER, new FluidPlaceEffect(Fluids.WATER.builtInRegistryHolder(), 10 * FluidType.BUCKET_VOLUME, 100));
        context.register(Sigils.VOID, new FluidRemoveEffect(1 * FluidType.BUCKET_VOLUME, 50));
        context.register(Sigils.MINER, new ApplyPotionEffect(
                MobEffects.DIG_SPEED,
                0,
                40,
                100,
                Optional.of(2),
                Optional.of(600),
                Optional.of(100)
        ));
    }

    public static void translations(BiConsumer<String, String> translator) {
        translator.accept("item.bloodmagic.sigil.invalid", "INVALID - NO TYPE");

        translator.accept("item.bloodmagic.sigil.divination", "Divination Sigil");
        translator.accept("tooltip.bloodmagic.sigil.divination", "Peer into the soul");

        translator.accept("item.bloodmagic.sigil.seer", "Seer Sigil");
        translator.accept("tooltip.bloodmagic.sigil.seer", "When seeing all is not enough.");

        translator.accept("item.bloodmagic.sigil.lava", "Lava Sigil");
        translator.accept("tooltip.bloodmagic.sigil.lava", "HOT! DO NOT EAT!");

        translator.accept("item.bloodmagic.sigil.water", "Water Sigil");
        translator.accept("tooltip.bloodmagic.sigil.water", "Infinite water, anyone?");

        translator.accept("item.bloodmagic.sigil.void", "Void Sigil");
        translator.accept("tooltip.bloodmagic.sigil.void", "Better than a Swiffer®!");

        translator.accept("item.bloodmagic.sigil.miner", "Sigil of the Fast Miner");
        translator.accept("tooltip.bloodmagic.sigil.miner", "Keep mining, and mining...");

        translator.accept("tooltip.bloodmagic.sigil.activated", "Activated");
        translator.accept("tooltip.bloodmagic.sigil.deactivated", "Deactivated");

        translator.accept("chat.bloodmagic.divination.current_essence", "Current Essence: %s");
        translator.accept("chat.bloodmagic.divination.other_network", "Peering into the Soul of %s");

        translator.accept("chat.bloodmagic.divination.altar.tier", "Current Tier: %s");
        translator.accept("chat.bloodmagic.divination.altar.essence", "Current Essence in main tank: %s");
        translator.accept("chat.bloodmagic.divination.altar.max_essence", "Main Essence tank capacity: %s");
    }
}
