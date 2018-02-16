package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.inversion.CorruptionHandler;
import net.minecraft.init.Blocks;

public class ModCorruptionBlocks {
    public static void init() {
        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            CorruptionHandler.registerBlockCorruption(type, Blocks.STONE, 0, RegistrarBloodMagicBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
            CorruptionHandler.registerBlockCorruption(type, Blocks.GRASS, 0, RegistrarBloodMagicBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
            CorruptionHandler.registerBlockCorruption(type, Blocks.DIRT, 0, RegistrarBloodMagicBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
        }
    }
}
