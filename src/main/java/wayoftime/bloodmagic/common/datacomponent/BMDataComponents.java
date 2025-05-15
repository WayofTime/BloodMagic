package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;

public class BMDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BloodMagic.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> DEMON_WILL_AMOUNT = DATA_COMPONENTS.registerComponentType("will_amount", builder -> builder.persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnumWillType>> DEMON_WILL_TYPE = DATA_COMPONENTS.registerComponentType("will_type", builder -> builder.persistent(EnumWillType.CODEC).networkSynchronized(EnumWillType.STREAM_CODEC));

    public static void register(IEventBus modBus) {
        DATA_COMPONENTS.register(modBus);
    }
}
