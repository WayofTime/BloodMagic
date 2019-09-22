package WayofTime.bloodmagic.core.data;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BMWorldSavedData extends WorldSavedData {
    public static final String ID = "BloodMagic-SoulNetworks";

    private Map<UUID, SoulNetwork> soulNetworks = new HashMap<>();

    public BMWorldSavedData(String id) {
        super(id);
    }

    public BMWorldSavedData() {
        this(ID);
    }

    public SoulNetwork getNetwork(PlayerEntity player) {
        return getNetwork(PlayerHelper.getUUIDFromPlayer(player));
    }

    public SoulNetwork getNetwork(UUID playerId) {
        if (!soulNetworks.containsKey(playerId))
            soulNetworks.put(playerId, SoulNetwork.newEmpty(playerId).setParent(this));
        return soulNetworks.get(playerId);
    }

    @Override
    public void readFromNBT(CompoundNBT tagCompound) {
        ListNBT networkData = tagCompound.getTagList("networkData", 10);

        for (int i = 0; i < networkData.tagCount(); i++) {
            CompoundNBT data = networkData.getCompoundTagAt(i);
            SoulNetwork network = SoulNetwork.fromNBT(data);
            network.setParent(this);
            soulNetworks.put(network.getPlayerId(), network);
        }
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT tagCompound) {
        ListNBT networkData = new ListNBT();
        for (SoulNetwork soulNetwork : soulNetworks.values())
            networkData.appendTag(soulNetwork.serializeNBT());

        tagCompound.setTag("networkData", networkData);

        return tagCompound;
    }
}
