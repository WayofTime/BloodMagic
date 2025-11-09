package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import org.apache.logging.log4j.util.TriConsumer;

public record AttributeEffect(ResourceLocation id, Holder<Attribute> attribute, AttributeModifier.Operation operation, LevelBasedValue amounts) {
    public static final MapCodec<AttributeEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AttributeEffect::id),
            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEffect::attribute),
            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeEffect::operation),
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(AttributeEffect::amounts)
    ).apply(builder, AttributeEffect::new));

    public void getModifier(int level, TriConsumer<Holder<Attribute>, AttributeModifier, EquipmentSlotGroup> modifierList) {
        modifierList.accept(
                attribute,
                new AttributeModifier(id, amounts.calculate(level), operation),
                EquipmentSlotGroup.CHEST
        );
    }
}
