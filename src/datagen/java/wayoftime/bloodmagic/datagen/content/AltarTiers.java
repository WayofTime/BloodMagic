package wayoftime.bloodmagic.datagen.content;

import net.minecraft.core.BlockPos;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.registry.AltarComponent;
import wayoftime.bloodmagic.common.registry.AltarTier;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AltarTiers {

    public static void bootstrap(BootstrapContext<AltarTier> builder) {
        builder.register(Keys.WEAK, new AltarTier(0, AltarTiers.WEAK));
        builder.register(Keys.APPRENTICE, new AltarTier(1, AltarTiers.APPRENTICE));
        builder.register(Keys.MAGE, new AltarTier(2, AltarTiers.MAGE));
        builder.register(Keys.MASTER, new AltarTier(3, AltarTiers.MASTER));
        builder.register(Keys.ARCHMAGE, new AltarTier(4, AltarTiers.ARCHMAGE));
        builder.register(Keys.TRANSCENDENT, new AltarTier(5, AltarTiers.TRANSCENDENT));
    }

    public static void tags(Function<TagKey<AltarTier>, TagsProvider.TagAppender<AltarTier>> setter) {
        setter.apply(BMTags.Tiers.VALID_TIERS)
                .add(Keys.WEAK)
                .add(Keys.APPRENTICE)
                .add(Keys.MAGE)
                .add(Keys.MASTER)
                .add(Keys.ARCHMAGE)
                .add(Keys.TRANSCENDENT);
    }

    protected static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }

    public static class Keys {
        public static final ResourceKey<AltarTier> WEAK = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.WEAK);
        public static final ResourceKey<AltarTier> APPRENTICE = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.APPRENTICE);
        public static final ResourceKey<AltarTier> MAGE = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.MAGE);
        public static final ResourceKey<AltarTier> MASTER = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.MASTER);
        public static final ResourceKey<AltarTier> ARCHMAGE = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.ARCHMAGE);
        public static final ResourceKey<AltarTier> TRANSCENDENT = ResourceKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, Locs.TRANSCENDENT);
    }

    public static class Locs {
        public static final ResourceLocation WEAK = bm("weak");
        public static final ResourceLocation APPRENTICE = bm("apprentice");
        public static final ResourceLocation MAGE = bm("mage");
        public static final ResourceLocation MASTER = bm("master");
        public static final ResourceLocation ARCHMAGE = bm("archmage");
        public static final ResourceLocation TRANSCENDENT = bm("transcendent");
    }

    private static final TagOrElementLocation ALTAR = new TagOrElementLocation(BMBlocks.BLOOD_ALTAR.block().getId(), false);

    private static final TagOrElementLocation PILLAR = new TagOrElementLocation(BMTags.Blocks.PILLARS.location(), true);
    private static final TagOrElementLocation RUNE = new TagOrElementLocation(BMTags.Blocks.RUNES.location(), true);
    private static final TagOrElementLocation T3_CAP = new TagOrElementLocation(BMTags.Blocks.T3_CAPSTONES.location(), true);
    private static final TagOrElementLocation T4_CAP = new TagOrElementLocation(BMTags.Blocks.T4_CAPSTONES.location(), true);
    private static final TagOrElementLocation T5_CAP = new TagOrElementLocation(BMTags.Blocks.T5_CAPSTONES.location(), true);
    private static final TagOrElementLocation T6_CAP = new TagOrElementLocation(BMTags.Blocks.T6_CAPSTONES.location(), true);

    public static List<AltarComponent> WEAK = List.of(new AltarComponent(new BlockPos(0, 0, 0), ALTAR, false));

    public static List<AltarComponent> APPRENTICE = new ArrayList<>();
    static {
        APPRENTICE.addAll(WEAK);

        APPRENTICE.add(new AltarComponent(new BlockPos(1, -1, 0), RUNE, true));
        APPRENTICE.add(new AltarComponent(new BlockPos(0, -1, 1), RUNE, true));
        APPRENTICE.add(new AltarComponent(new BlockPos(-1, -1, 0), RUNE, true));
        APPRENTICE.add(new AltarComponent(new BlockPos(0, -1, -1), RUNE, true));

        APPRENTICE.add(new AltarComponent(new BlockPos(1, -1, 1), RUNE, false));
        APPRENTICE.add(new AltarComponent(new BlockPos(1, -1, -1), RUNE, false));
        APPRENTICE.add(new AltarComponent(new BlockPos(-1, -1, 1), RUNE, false));
        APPRENTICE.add(new AltarComponent(new BlockPos(-1, -1, -1), RUNE, false));
    }

    public static List<AltarComponent> MAGE = new ArrayList<>();
    static {
        MAGE.addAll(WEAK);

        MAGE.add(new AltarComponent(new BlockPos(1, -1, 1), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(1, -1, 0), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(1, -1, -1), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(0, -1, 1), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(0, -1, -1), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(-1, -1, 1), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(-1, -1, 0), RUNE, true));
        MAGE.add(new AltarComponent(new BlockPos(-1, -1, -1), RUNE, true));

        for (int i = -2; i <= 2; i++) {
            MAGE.add(new AltarComponent(new BlockPos(i, -2, 3), RUNE, true));
            MAGE.add(new AltarComponent(new BlockPos(i, -2, -3), RUNE, true));
            MAGE.add(new AltarComponent(new BlockPos(3, -2, i), RUNE, true));
            MAGE.add(new AltarComponent(new BlockPos(-3, -2, i), RUNE, true));
        }

        for (int i = -1; i <= 1; i++) {
            MAGE.add(new AltarComponent(new BlockPos(3, i, 3), PILLAR, false));
            MAGE.add(new AltarComponent(new BlockPos(3, i, -3), PILLAR, false));
            MAGE.add(new AltarComponent(new BlockPos(-3, i, 3), PILLAR, false));
            MAGE.add(new AltarComponent(new BlockPos(-3, i, -3), PILLAR, false));
        }

        MAGE.add(new AltarComponent(new BlockPos(3, 1, 3), T3_CAP, false));
        MAGE.add(new AltarComponent(new BlockPos(3, 1, -3), T3_CAP, false));
        MAGE.add(new AltarComponent(new BlockPos(-3, 1, 3), T3_CAP, false));
        MAGE.add(new AltarComponent(new BlockPos(-3, 1, -3), T3_CAP, false));
    }

    public static List<AltarComponent> MASTER = new ArrayList<>();
    static {
        MASTER.addAll(MAGE);

        for (int i = -3; i <= 3; i++) {
            MASTER.add(new AltarComponent(new BlockPos(i, -3, 5), RUNE, true));
            MASTER.add(new AltarComponent(new BlockPos(i, -3, -5), RUNE, true));
            MASTER.add(new AltarComponent(new BlockPos(5, -3, i), RUNE, true));
            MASTER.add(new AltarComponent(new BlockPos(-5, -3, i), RUNE, true));
        }

        for (int i = -2; i <= 1; i++) {
            MASTER.add(new AltarComponent(new BlockPos(5, i, 5), PILLAR, false));
            MASTER.add(new AltarComponent(new BlockPos(5, i, -5), PILLAR, false));
            MASTER.add(new AltarComponent(new BlockPos(-5, i, 5), PILLAR, false));
            MASTER.add(new AltarComponent(new BlockPos(-5, i, -5), PILLAR, false));
        }
        MASTER.add(new AltarComponent(new BlockPos(5, 2, 5), T4_CAP, false));
        MASTER.add(new AltarComponent(new BlockPos(5, 2, -5), T4_CAP, false));
        MASTER.add(new AltarComponent(new BlockPos(-5, 2, 5), T4_CAP, false));
        MASTER.add(new AltarComponent(new BlockPos(-5, 2, -5), T4_CAP, false));
    }

    public static List<AltarComponent> ARCHMAGE = new ArrayList<>();
    static {
        ARCHMAGE.addAll(MASTER);

        for (int i = -6; i <= 6; i++) {
            ARCHMAGE.add(new AltarComponent(new BlockPos(i, -4, 8), RUNE, true));
            ARCHMAGE.add(new AltarComponent(new BlockPos(i, -4, -8), RUNE, true));
            ARCHMAGE.add(new AltarComponent(new BlockPos(8, -4, i), RUNE, true));
            ARCHMAGE.add(new AltarComponent(new BlockPos(-8, -4, i), RUNE, true));
        }

        ARCHMAGE.add(new AltarComponent(new BlockPos(8, -4, 8), T5_CAP, false));
        ARCHMAGE.add(new AltarComponent(new BlockPos(8, -4, -8), T5_CAP, false));
        ARCHMAGE.add(new AltarComponent(new BlockPos(-8, -4, 8), T5_CAP, false));
        ARCHMAGE.add(new AltarComponent(new BlockPos(-8, -4, -8), T5_CAP, false));
    }

    public static List<AltarComponent> TRANSCENDENT = new ArrayList<>();
    static {
        TRANSCENDENT.addAll(ARCHMAGE);

        for (int i = -9; i <= 9; i++) {
            TRANSCENDENT.add(new AltarComponent(new BlockPos(i, -5, 11), RUNE, true));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(i, -5, -11), RUNE, true));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(11, -5, i), RUNE, true));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(-11, -5, i), RUNE, true));
        }

        for (int i = -4; i <= 2; i++) {
            TRANSCENDENT.add(new AltarComponent(new BlockPos(11, i, 11), PILLAR, false));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(11, i, -11), PILLAR, false));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(-11, i, 11), PILLAR, false));
            TRANSCENDENT.add(new AltarComponent(new BlockPos(-11, i, -11), PILLAR, false));
        }

        TRANSCENDENT.add(new AltarComponent(new BlockPos(11, 3, 11), T6_CAP, false));
        TRANSCENDENT.add(new AltarComponent(new BlockPos(11, 3, -11), T6_CAP, false));
        TRANSCENDENT.add(new AltarComponent(new BlockPos(-11, 3, 11), T6_CAP, false));
        TRANSCENDENT.add(new AltarComponent(new BlockPos(-11, 3, -11), T6_CAP, false));
    }
}
