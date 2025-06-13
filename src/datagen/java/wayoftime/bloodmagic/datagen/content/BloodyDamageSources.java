package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import wayoftime.bloodmagic.common.damagesource.BMDamageSources;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.function.Function;

public class BloodyDamageSources {
    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BMDamageSources.SACRIFICE, new DamageType("sacrifice", DamageScaling.NEVER, 0F));
        context.register(BMDamageSources.SELF_SACRIFICE, new DamageType("self_sacrifice", DamageScaling.NEVER, 0F));
    }

    public static void tags(Function<TagKey<DamageType>, TagsProvider.TagAppender<DamageType>> setter) {
        setter.apply(DamageTypeTags.BYPASSES_ARMOR)
                .add(BMDamageSources.SELF_SACRIFICE)
                .add(BMDamageSources.SACRIFICE);

        setter.apply(DamageTypeTags.BYPASSES_EFFECTS)
                .add(BMDamageSources.SELF_SACRIFICE)
                .add(BMDamageSources.SACRIFICE);

        setter.apply(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .add(BMDamageSources.SELF_SACRIFICE)
                .add(BMDamageSources.SACRIFICE);

        setter.apply(DamageTypeTags.NO_IMPACT)
                .add(BMDamageSources.SELF_SACRIFICE)
                .add(BMDamageSources.SACRIFICE);

        setter.apply(DamageTypeTags.NO_KNOCKBACK)
                .add(BMDamageSources.SELF_SACRIFICE)
                .add(BMDamageSources.SACRIFICE);

        setter.apply(BMTags.DamageTypes.SELF_SACRIFICE)
                .add(BMDamageSources.SELF_SACRIFICE); // needed later for damage predicates

        setter.apply(BMTags.DamageTypes.TOUGH_IGNORED)
                .addTag(DamageTypeTags.IS_FIRE)
                .addTag(DamageTypeTags.IS_EXPLOSION)
                .addTag(DamageTypeTags.IS_FALL)
                .addTag(DamageTypeTags.IS_PROJECTILE);
    }
}
