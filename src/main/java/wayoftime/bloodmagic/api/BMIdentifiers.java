package wayoftime.bloodmagic.api;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class BMIdentifiers {
    public static class ImperfectRituals {
        public static final ResourceLocation DAY_RITUAL = bm("day");
        public static final ResourceLocation NIGHT_RITUAL = bm("night");
        public static final ResourceLocation RESISTANCE_RITUAL = bm("resistance");
        public static final ResourceLocation ZOMBIE_RITUAL = bm("zombie");
    }

    // TODO move all the stuff here, including from Datagen

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }
}
