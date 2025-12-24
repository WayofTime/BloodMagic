package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.living.LivingUpgrade;

public record LivingArmorData(TagKey<Item> requiredSet, TagKey<LivingUpgrade> startingUpgrades, TagKey<LivingUpgrade> blacklist) {
    public static final Codec<LivingArmorData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            TagKey.codec(Registries.ITEM).fieldOf("required_set").forGetter(LivingArmorData::requiredSet),
            TagKey.codec(BMIdentifiers.RegistryKeys.LIVING_UPGRADES).fieldOf("starting_upgrades").forGetter(LivingArmorData::startingUpgrades),
            TagKey.codec(BMIdentifiers.RegistryKeys.LIVING_UPGRADES).fieldOf("blacklist").forGetter(LivingArmorData::blacklist)
    ).apply(builder, LivingArmorData::new));
}
