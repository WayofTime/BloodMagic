package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Data component for storing anointments on items (charges, weapons, tools).
 * Replaces the old NBT-based anointment_holder system.
 */
public record AnointmentHolder(List<AnointmentEntry> anointments) {

    public static final Codec<AnointmentHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(AnointmentEntry.CODEC).fieldOf("anointments").forGetter(AnointmentHolder::anointments)
    ).apply(instance, AnointmentHolder::new));

    public static final StreamCodec<ByteBuf, AnointmentHolder> STREAM_CODEC = StreamCodec.composite(
            AnointmentEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            AnointmentHolder::anointments,
            AnointmentHolder::new
    );

    public static AnointmentHolder empty() {
        return new AnointmentHolder(List.of());
    }

    public static AnointmentHolder single(ResourceLocation key, int level, int maxDamage) {
        return new AnointmentHolder(List.of(new AnointmentEntry(key, level, 0, maxDamage)));
    }

    public static AnointmentHolder single(String key, int level, int maxDamage) {
        return single(ResourceLocation.parse(key), level, maxDamage);
    }

    public boolean isEmpty() {
        return anointments.isEmpty();
    }

    public boolean hasAnointment(ResourceLocation key) {
        return anointments.stream().anyMatch(a -> a.key().equals(key));
    }

    public AnointmentHolder withDamage(ResourceLocation key, int damage) {
        List<AnointmentEntry> newList = new ArrayList<>();
        for (AnointmentEntry entry : anointments) {
            if (entry.key().equals(key)) {
                newList.add(new AnointmentEntry(entry.key(), entry.level(), entry.damage() + damage, entry.maxDamage()));
            } else {
                newList.add(entry);
            }
        }
        return new AnointmentHolder(newList);
    }

    /**
     * A single anointment entry with its key, level, damage, and max damage.
     */
    public record AnointmentEntry(ResourceLocation key, int level, int damage, int maxDamage) {

        public static final Codec<AnointmentEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(AnointmentEntry::key),
                Codec.INT.fieldOf("level").forGetter(AnointmentEntry::level),
                Codec.INT.fieldOf("damage").forGetter(AnointmentEntry::damage),
                Codec.INT.fieldOf("max_damage").forGetter(AnointmentEntry::maxDamage)
        ).apply(instance, AnointmentEntry::new));

        public static final StreamCodec<ByteBuf, AnointmentEntry> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, AnointmentEntry::key,
                ByteBufCodecs.INT, AnointmentEntry::level,
                ByteBufCodecs.INT, AnointmentEntry::damage,
                ByteBufCodecs.INT, AnointmentEntry::maxDamage,
                AnointmentEntry::new
        );

        public boolean isExpired() {
            return damage >= maxDamage;
        }

        public int remainingUses() {
            return maxDamage - damage;
        }
    }
}
