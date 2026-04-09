package wayoftime.bloodmagic.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import wayoftime.bloodmagic.api.ritual.Range;

import javax.annotation.Nullable;

public class RitualUtil {

    public static void spawnLightning(ServerLevel level, BlockPos pos, boolean visualOnly) {
        Entity lightning = EntityType.LIGHTNING_BOLT.create(level, bolt -> bolt.setVisualOnly(visualOnly), pos, MobSpawnType.TRIGGERED, false, false);
        if (lightning != null) {
            level.addFreshEntity(lightning);
        }
    }

    public static @Nullable IItemHandler getItemHandler(Range range, Level level) {
        while (range.hasNext()) {
            IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, range.next(), Direction.DOWN);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
