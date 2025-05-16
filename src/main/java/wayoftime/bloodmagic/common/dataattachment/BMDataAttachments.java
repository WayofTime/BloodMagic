package wayoftime.bloodmagic.common.dataattachment;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;

import java.util.HashMap;
import java.util.Map;

public class BMDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, BloodMagic.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> INCENSE = ATTACHMENT_TYPES.register(
            "incense", () -> AttachmentType.builder(() -> 0D).serialize(Codec.DOUBLE).build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<ResourceLocation, Integer>>> LIVING_COOLDOWN = ATTACHMENT_TYPES.register("living_cooldown", () -> AttachmentType.<Map<ResourceLocation, Integer>>builder(() -> new HashMap<>()).serialize(Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT)).build());

    public static void register(IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }
}
