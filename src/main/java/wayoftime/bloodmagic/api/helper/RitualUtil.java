package wayoftime.bloodmagic.api.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class RitualUtil {

    public static void spawnLightning(ServerLevel level, BlockPos pos, boolean visualOnly) {
        Entity lightning = EntityType.LIGHTNING_BOLT.create(level, bolt -> bolt.setVisualOnly(visualOnly), pos, MobSpawnType.TRIGGERED, false, false);
        if (lightning != null) {
            level.addFreshEntity(lightning);
        }
    }
}
