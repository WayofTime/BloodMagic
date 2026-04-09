package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.altar.AltarTier;
import wayoftime.bloodmagic.api.BMTags;

public class AltarTiers {

    public static void tiers(BootstrapContext<AltarTier> builder) {
        builder.register(Keys.APPRENTICE, APPRENTICE);
        builder.register(Keys.MAGE, MAGE);
        builder.register(Keys.MASTER, MASTER);
        builder.register(Keys.ARCHMAGE, ARCHMAGE);
        builder.register(Keys.TRANSCENDENT, TRANSCENDENT);
    }

    protected static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }

    public static class Keys {
        public static final ResourceKey<AltarTier> WEAK = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.WEAK);
        public static final ResourceKey<AltarTier> APPRENTICE = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.APPRENTICE);
        public static final ResourceKey<AltarTier> MAGE = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.MAGE);
        public static final ResourceKey<AltarTier> MASTER = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.MASTER);
        public static final ResourceKey<AltarTier> ARCHMAGE = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.ARCHMAGE);
        public static final ResourceKey<AltarTier> TRANSCENDENT = ResourceKey.create(BMIdentifiers.RegistryKeys.ALTAR_TIER_KEY, Locs.TRANSCENDENT);
    }

    public static class Locs {
        public static final ResourceLocation WEAK = bm("weak");
        public static final ResourceLocation APPRENTICE = bm("apprentice");
        public static final ResourceLocation MAGE = bm("mage");
        public static final ResourceLocation MASTER = bm("master");
        public static final ResourceLocation ARCHMAGE = bm("archmage");
        public static final ResourceLocation TRANSCENDENT = bm("transcendent");
    }

    private static final TagOrElementLocation T3_CAP = new TagOrElementLocation(BMTags.Blocks.T3_CAPSTONES.location(), true);
    private static final TagOrElementLocation T4_CAP = new TagOrElementLocation(BMTags.Blocks.T4_CAPSTONES.location(), true);
    private static final TagOrElementLocation T5_CAP = new TagOrElementLocation(BMTags.Blocks.T5_CAPSTONES.location(), true);
    private static final TagOrElementLocation T6_CAP = new TagOrElementLocation(BMTags.Blocks.T6_CAPSTONES.location(), true);
    private static final TagOrElementLocation AIR = new TagOrElementLocation(ResourceLocation.withDefaultNamespace("air"), false);

    public static AltarTier APPRENTICE = new AltarTier(2, 1, 1, 0, 0, 0, AIR);
    public static AltarTier MAGE = new AltarTier(3, 2, 2, 0, 2, 0, T3_CAP);
    public static AltarTier MASTER = new AltarTier(4, 2, 3, 1, 4, 0, T4_CAP);
    public static AltarTier ARCHMAGE = new AltarTier(5, 3, 6, 1, 0, -1, T5_CAP);
    public static AltarTier TRANSCENDENT = new AltarTier(6, 3, 9, 1, 6, 0, T6_CAP);
}
