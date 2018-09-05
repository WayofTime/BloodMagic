package com.wayoftime.bloodmagic.core.network;

import com.google.common.collect.EvictingQueue;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.BooleanResult;
import com.wayoftime.bloodmagic.event.SoulNetworkEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Queue;
import java.util.UUID;

public class SoulNetwork implements INBTSerializable<NBTTagCompound> {

    public static final DamageSource WEAK_SOUL = new DamageSource(BloodMagic.MODID + ":weak_soul").setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();

    private final Queue<NetworkInteraction> interactionHistory;
    private SNSavedData parent;
    private UUID ownerId;
    private int currentEssence;
    private int currentTier;
    private int maxForTier;

    private SoulNetwork() {
        this.interactionHistory = EvictingQueue.create(16);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("owner", ownerId.toString());
        tag.setInteger("essence", currentEssence);
        tag.setInteger("tier", currentTier);
        tag.setInteger("maxForTier", maxForTier);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.ownerId = UUID.fromString(nbt.getString("owner"));
        this.currentEssence = nbt.getInteger("essence");
        this.currentTier = nbt.getInteger("tier");
        this.maxForTier = nbt.getInteger("maxForTier");
    }

    public SoulNetwork withParent(SNSavedData parent) {
        this.parent = parent;
        return this;
    }

    public SNSavedData getParent() {
        return parent;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public BooleanResult<Integer> submitInteraction(NetworkInteraction interaction) {
        if (interaction.getAmount() == 0)
            return new BooleanResult<>(0, true);

        if (!interaction.isSyphon()) {
            SoulNetworkEvent.Fill event = new SoulNetworkEvent.Fill(this, interaction, maxForTier);
            if (MinecraftForge.EVENT_BUS.post(event))
                return new BooleanResult<>(0, false);

            if (currentEssence > event.getMaximum())
                return new BooleanResult<>(0, false);

            int oldCurrent = currentEssence;
            currentEssence = Math.min(event.getMaximum(), currentEssence + event.getTicket().getAmount());
            markDirty();
            interactionHistory.remove(interaction); // Allows an existing interaction to be moved back up to the top with whatever new amount is changed
            interactionHistory.add(interaction);
            return new BooleanResult<>(currentEssence - oldCurrent, true);
        } else {
            SoulNetworkEvent.Syphon event = new SoulNetworkEvent.Syphon(this, interaction);
            if (MinecraftForge.EVENT_BUS.post(event))
                return new BooleanResult<>(0, false);

            int newEssence = currentEssence - event.getTicket().getAmount();
            if (newEssence < 0) {
                currentEssence = 0;
                markDirty();
                return new BooleanResult<>(currentEssence, false);
            }

            currentEssence -= event.getTicket().getAmount();
            markDirty();
            interactionHistory.remove(interaction);
            interactionHistory.add(interaction);
            return new BooleanResult<>(event.getTicket().getAmount(), true);
        }
    }

    public int getEssence() {
        return currentEssence;
    }

    public SoulNetwork setEssence(int essence) {
        this.currentEssence = Math.min(this.maxForTier, essence);
        markDirty();
        return this;
    }

    public SoulNetwork setTier(int newTier, int maxForTier) {
        this.currentTier = newTier;
        this.maxForTier = maxForTier;
        markDirty();
        return this;
    }

    public int getTier() {
        return currentTier;
    }

    public void markDirty() {
        if (parent != null)
            parent.markDirty();
    }

    public static SoulNetwork get(UUID uuid) {
        return SNSavedData.getSoulNetwork(uuid);
    }

    public static SoulNetwork newEmpty(UUID uuid) {
        SoulNetwork network = new SoulNetwork();
        network.ownerId = uuid;
        return network;
    }

    public static SoulNetwork fromNbt(NBTTagCompound tag) {
        SoulNetwork network = new SoulNetwork();
        network.deserializeNBT(tag);
        return network;
    }
}
