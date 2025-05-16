package wayoftime.bloodmagic.common.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BMSavedData extends SavedData {
    public static final String ID = "bloodmagic";

    private Map<UUID, SoulNetwork> soulNetworks = new HashMap<>();

    public SoulNetwork getNetwork(UUID playerId) {
        if (!soulNetworks.containsKey(playerId))
            soulNetworks.put(playerId, SoulNetwork.newEmpty(playerId, this));

        return soulNetworks.get(playerId);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag networkData = new ListTag();
        for (SoulNetwork soulNetwork : soulNetworks.values()) {
            networkData.add(soulNetwork.toNBT());
        }

        tag.put("networkData", networkData);

        return tag;
    }

    public static BMSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        BMSavedData savedData = new BMSavedData();
        ListTag networkData = tag.getList("networkData", Tag.TAG_COMPOUND);

        for (int i = 0; i < networkData.size(); i++) {
            CompoundTag data = networkData.getCompound(i);
            SoulNetwork network = SoulNetwork.fromNBT(data, savedData);
            savedData.soulNetworks.put(network.getPlayerId(), network);
        }

        return savedData;
    }
}
