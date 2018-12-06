package WayofTime.bloodmagic.core.data;

import WayofTime.bloodmagic.event.SoulNetworkEvent;
import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.BooleanResult;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class SoulNetwork implements INBTSerializable<NBTTagCompound> {

    private final Queue<SoulTicket> ticketHistory;
    private BMWorldSavedData parent;
    private EntityPlayer cachedPlayer;
    private UUID playerId;
    private int currentEssence;
    private int orbTier;

    private SoulNetwork() {
        // No-op - For creation via NBT only
        ticketHistory = EvictingQueue.create(16);
    }

    public void clear() {
        ticketHistory.clear();
    }

    public int add(SoulTicket ticket, int maximum) {
        SoulNetworkEvent.Fill event = new SoulNetworkEvent.Fill(this, ticket, maximum);
        if (MinecraftForge.EVENT_BUS.post(event))
            return 0;

        int currEss = getCurrentEssence();

        if (currEss >= event.getMaximum())
            return 0;

        int newEss = Math.min(event.getMaximum(), currEss + event.getTicket().getAmount());
        setCurrentEssence(newEss);

        if (ticketHistory.contains(ticket))
            ticketHistory.remove(ticket); // "Pops" the existing ticket to the top of the queue

        ticketHistory.add(ticket);

        return newEss - currEss;
    }

    /**
     * @deprecated For future proofing, use {@link #add(SoulTicket, int)} instead.
     */
    @Deprecated
    public int add(int toAdd, int maximum) {
        return add(new SoulTicket(toAdd), maximum);
    }

    /**
     * @deprecated Use {@link #add(SoulTicket, int)} instead.
     */
    @Deprecated
    public int addLifeEssence(int toAdd, int maximum) {
        return add(toAdd, maximum);
    }

    public int syphon(SoulTicket ticket) {
        return syphon(ticket, false);
    }

    public int syphon(SoulTicket ticket, boolean skipEvent) {
        SoulNetworkEvent.Syphon event = new SoulNetworkEvent.Syphon(this, ticket);
        if (!skipEvent && MinecraftForge.EVENT_BUS.post(event))
            return 0;

        int syphon = event.getTicket().getAmount();
        if (getCurrentEssence() >= syphon) {
            setCurrentEssence(getCurrentEssence() - syphon);
            if (ticketHistory.contains(ticket))
                ticketHistory.remove(ticket);

            ticketHistory.add(ticket);
            return syphon;
        }

        return 0;
    }

    /**
     * @deprecated For future proofing, use {@link #syphon(SoulTicket)} instead.
     */
    @Deprecated
    public int syphon(int amount) {
        return syphon(new SoulTicket(amount));
    }

    public BooleanResult<Integer> syphonAndDamage(EntityPlayer user, SoulTicket ticket) {
        if (user.getEntityWorld().isRemote)
            return BooleanResult.newResult(false, 0);

        SoulNetworkEvent.Syphon.User event = new SoulNetworkEvent.Syphon.User(this, ticket, user);

        if (MinecraftForge.EVENT_BUS.post(event))
            return BooleanResult.newResult(false, 0);

        int drainAmount = syphon(event.getTicket(), true);

        if (drainAmount <= 0 || event.shouldDamage())
            hurtPlayer(user, event.getTicket().getAmount());

        if (ticketHistory.contains(ticket))
            ticketHistory.remove(ticket);

        ticketHistory.add(ticket);

        return BooleanResult.newResult(true, event.getTicket().getAmount());
    }

    /**
     * @deprecated Use {@link #syphonAndDamage(EntityPlayer, SoulTicket)} instead.
     */
    @Deprecated
    public boolean syphonAndDamage(EntityPlayer user, int amount) {
        return syphonAndDamage(user, new SoulTicket(amount)).isSuccess();
    }

    public void causeNausea() {
        if (getPlayer() != null)
            getPlayer().addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 99));
    }

    /**
     * @deprecated - Please use {@link #causeNausea()}
     */
    @Deprecated
    public void causeNauseaToPlayer() {
        causeNausea();
    }

    public void hurtPlayer(EntityPlayer user, float syphon) {
        if (user != null) {
            if (syphon < 100 && syphon > 0) {
                if (!user.capabilities.isCreativeMode) {
                    user.hurtResistantTime = 0;
                    user.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, 1.0F);
                }

            } else if (syphon >= 100) {
                if (!user.capabilities.isCreativeMode) {
                    for (int i = 0; i < ((syphon + 99) / 100); i++) {
                        user.hurtResistantTime = 0;
                        user.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, 1.0F);
                    }
                }
            }
        }
    }

    private void markDirty() {
        if (getParent() != null)
            getParent().markDirty();
        else
            BMLog.DEFAULT.error("A SoulNetwork was created, but a parent was not set to allow saving.");
    }

    @Nullable
    public EntityPlayer getPlayer() {
        if (cachedPlayer == null)
            cachedPlayer = PlayerHelper.getPlayerFromUUID(playerId);

        return cachedPlayer;
    }

    public BMWorldSavedData getParent() {
        return parent;
    }

    public SoulNetwork setParent(BMWorldSavedData parent) {
        this.parent = parent;
        markDirty();
        return this;
    }

    public EntityPlayer getCachedPlayer() {
        return cachedPlayer;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getCurrentEssence() {
        return currentEssence;
    }

    public SoulNetwork setCurrentEssence(int currentEssence) {
        this.currentEssence = currentEssence;
        markDirty();
        return this;
    }

    public int getOrbTier() {
        return orbTier;
    }

    public SoulNetwork setOrbTier(int orbTier) {
        this.orbTier = orbTier;
        markDirty();
        return this;
    }

    public List<SoulTicket> getTicketHistory() {
        return ImmutableList.copyOf(ticketHistory);
    }

    // INBTSerializable

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("playerId", getPlayerId().toString());
        tagCompound.setInteger("currentEssence", getCurrentEssence());
        tagCompound.setInteger("orbTier", getOrbTier());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.playerId = UUID.fromString(nbt.getString("playerId"));
        this.currentEssence = nbt.getInteger("currentEssence");
        this.orbTier = nbt.getInteger("orbTier");
    }

    public static SoulNetwork fromNBT(NBTTagCompound tagCompound) {
        SoulNetwork soulNetwork = new SoulNetwork();
        soulNetwork.deserializeNBT(tagCompound);
        return soulNetwork;
    }

    public static SoulNetwork newEmpty(UUID uuid) {
        SoulNetwork network = new SoulNetwork();
        network.playerId = uuid;
        return network;
    }
}
