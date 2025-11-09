package wayoftime.bloodmagic.util.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData.Factory;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.common.world.BMSavedData;

import javax.annotation.Nullable;
import java.util.UUID;


@EventBusSubscriber
public class SoulNetworkHelper {
    @Nullable
    private static BMSavedData SD_INSTANCE;

    @SubscribeEvent
    public static void resetSavedDataInstance(ServerStoppedEvent event) {
        SD_INSTANCE = null;
    }

    public static SoulNetwork getSoulNetwork(UUID uuid) {
        if (SD_INSTANCE == null) {
            if (ServerLifecycleHooks.getCurrentServer() == null)
                return null;

            DimensionDataStorage dimData = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();
            SD_INSTANCE = dimData.computeIfAbsent(new Factory<>(BMSavedData::new, BMSavedData::load), BMSavedData.ID);
        }

        return SD_INSTANCE.getNetwork(uuid);
    }

    public static SoulNetwork getSoulNetwork(Binding binding) {
        return getSoulNetwork(binding.uuid());
    }

    public static SoulNetwork getSoulNetwork(Player player) {
        return getSoulNetwork(player.getUUID());
    }

    public static SoulNetwork getSoulNetwork(String uuid) {
        return getSoulNetwork(UUID.fromString(uuid));
    }
}
