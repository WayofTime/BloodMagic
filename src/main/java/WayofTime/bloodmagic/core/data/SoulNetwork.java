package WayofTime.bloodmagic.core.data;

import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.event.AddToNetworkEvent;
import WayofTime.bloodmagic.event.SoulNetworkEvent;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.UUID;

public class SoulNetwork implements INBTSerializable<NBTTagCompound> {
    private BMWorldSavedData parent;
    private EntityPlayer cachedPlayer;
    private UUID playerId;
    private int currentEssence;
    private int orbTier;

    private SoulNetwork() {
        // No-op - For creation via NBT only
    }

    public int add(int toAdd, int maximum) {
        AddToNetworkEvent event = new AddToNetworkEvent(playerId.toString(), toAdd, maximum);

        if (MinecraftForge.EVENT_BUS.post(event))
            return 0;

        if (FMLCommonHandler.instance().getMinecraftServerInstance() == null)
            return 0;

        int currEss = getCurrentEssence();

        if (currEss >= event.maximum)
            return 0;

        int newEss = Math.min(event.maximum, currEss + event.addedAmount);
        if (event.getResult() != Event.Result.DENY)
            setCurrentEssence(newEss);

        return newEss - currEss;
    }

    /**
     * @deprecated - Please use {@link #add(int, int)}
     */
    @Deprecated
    public int addLifeEssence(int toAdd, int maximum) {
        return add(toAdd, maximum);
    }

    public int syphon(int syphon) {
        if (getCurrentEssence() >= syphon) {
            setCurrentEssence(getCurrentEssence() - syphon);
            return syphon;
        }

        return 0;
    }

    public boolean syphonAndDamage(EntityPlayer user, int toSyphon) {
        if (user != null) {
            if (user.getEntityWorld().isRemote)
                return false;

            if (!Strings.isNullOrEmpty(playerId.toString())) {
                SoulNetworkEvent.ItemDrainNetworkEvent event = new SoulNetworkEvent.ItemDrainNetworkEvent(user, playerId, null, toSyphon);

                if (MinecraftForge.EVENT_BUS.post(event))
                    return false;

                int drainAmount = syphon(event.syphon);

                if (drainAmount <= 0 || event.shouldDamage)
                    hurtPlayer(user, event.syphon);

                return event.getResult() != Event.Result.DENY;
            }

            int amount = syphon(toSyphon);
            hurtPlayer(user, toSyphon - amount);

            return true;
        }

        return false;
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
