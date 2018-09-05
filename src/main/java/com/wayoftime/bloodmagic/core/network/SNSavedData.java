package com.wayoftime.bloodmagic.core.network;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class SNSavedData extends WorldSavedData {

    public static final String ID = "bloodmagic_soul_network_data";

    private final Map<UUID, SoulNetwork> networks = Maps.newHashMap();

    public SNSavedData(String name) {
        super(name);
    }

    public SNSavedData() {
        this(ID);
    }

    public SoulNetwork getNetwork(EntityPlayer player) {
        return getNetwork(player.getGameProfile().getId());
    }

    public SoulNetwork getNetwork(UUID uuid) {
        SoulNetwork network = networks.get(uuid);
        if (network == null) {
            networks.put(uuid, network = SoulNetwork.newEmpty(uuid).withParent(this));
            markDirty();
        }

        return network;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList networks = nbt.getTagList("networks", 10);
        for (NBTBase tag : networks) {
            if (tag.getId() != 10) // Only compounds
                continue;

            SoulNetwork network = SoulNetwork.fromNbt((NBTTagCompound) tag);
            network.withParent(this);
            this.networks.put(network.getOwnerId(), network);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList networks = new NBTTagList();
        for (SoulNetwork network : this.networks.values())
            networks.appendTag(network.serializeNBT());

        compound.setTag("networks", networks);
        return compound;
    }

    @Nonnull
    public static SoulNetwork getSoulNetwork(UUID uuid) {
        World world = DimensionManager.getWorld(0);
        if (world == null || world.getMapStorage() == null)
            return new SNSavedData().getNetwork(uuid);

        SNSavedData savedData = (SNSavedData) world.getMapStorage().getOrLoadData(SNSavedData.class, ID);
        if (savedData == null) {
            savedData = new SNSavedData();
            world.getMapStorage().setData(ID, savedData);
        }

        return savedData.getNetwork(uuid);
    }
}
