package WayofTime.bloodmagic.registry;

import net.minecraft.init.Blocks;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.inversion.CorruptionHandler;

public class ModCorruptionBlocks
{
    public static void init()
    {
        for (EnumDemonWillType type : EnumDemonWillType.values())
        {
            CorruptionHandler.registerBlockCorruption(type, Blocks.STONE, 0, ModBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
            CorruptionHandler.registerBlockCorruption(type, Blocks.GRASS, 0, ModBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
            CorruptionHandler.registerBlockCorruption(type, Blocks.DIRT, 0, ModBlocks.DEMON_EXTRAS.getStateFromMeta(type.ordinal()));
        }
    }
}
