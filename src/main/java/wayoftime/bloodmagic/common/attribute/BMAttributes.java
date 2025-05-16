package wayoftime.bloodmagic.common.attribute;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;

public class BMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, BloodMagic.MODID);

    public static final DeferredHolder<Attribute, PercentageAttribute> SELF_SACRIFICE_MULTIPLIER = ATTRIBUTES.register("player.self_sacrifice_multiplier", () -> new PercentageAttribute("attribute.bloodmagic.player.self_sacrifice", 1, 0, 100));

    private static void addToPlayer(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, SELF_SACRIFICE_MULTIPLIER, 1);
    }

    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
        modBus.addListener(BMAttributes::addToPlayer);
    }
}
