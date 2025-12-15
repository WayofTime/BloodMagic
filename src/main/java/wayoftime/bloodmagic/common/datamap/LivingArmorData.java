package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.BMRegistries;

public record LivingArmorData(TagKey<Item> requiredSet, TagKey<LivingUpgrade> startingUpgrades, TagKey<LivingUpgrade> blacklist) {
    public static final Codec<LivingArmorData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            TagKey.codec(Registries.ITEM).fieldOf("required_set").forGetter(LivingArmorData::requiredSet),
            TagKey.codec(BMRegistries.Keys.LIVING_UPGRADES).fieldOf("starting_upgrades").forGetter(LivingArmorData::startingUpgrades),
            TagKey.codec(BMRegistries.Keys.LIVING_UPGRADES).fieldOf("blacklist").forGetter(LivingArmorData::blacklist)
    ).apply(builder, LivingArmorData::new));
}
