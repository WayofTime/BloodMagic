package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.world.BMSavedData;
import wayoftime.bloodmagic.common.damagesource.BMDamageSources;
import wayoftime.bloodmagic.util.SoulTicket;

import java.util.UUID;

public class SoulNetwork {
    public static final Codec<SoulNetwork> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(SoulNetwork::getPlayerId),
            Codec.INT.fieldOf("current_essence").forGetter(SoulNetwork::getCurrentEssence)
    ).apply(builder, SoulNetwork::new));

    private UUID playerId;
    private int currentEssence;
    private BMSavedData parent;

    public static SoulNetwork newEmpty(UUID playerId, BMSavedData parent) {
        SoulNetwork soulNetwork = new SoulNetwork(playerId, 0);
        soulNetwork.parent = parent;
        return soulNetwork;
    }

    protected SoulNetwork(UUID playerId, int essence) {
        this.playerId = playerId;
        this.currentEssence = essence;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public int getCurrentEssence() {
        return currentEssence;
    }

    private void setCurrentEssence(int currentEssence) {
        this.currentEssence = currentEssence;
        markDirty();
    }

    private void markDirty() {
        if (parent != null)
            parent.setDirty();
    }

    public static SoulNetwork fromNBT(CompoundTag tag, BMSavedData parent) {
        SoulNetwork soulNetwork = CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();

        soulNetwork.parent = parent;

        return soulNetwork;
    }

    public CompoundTag toNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
    }

    public int add(SoulTicket ticket, int maximum) {
        int curr = getCurrentEssence();
        if (curr >= maximum)
            return 0;

        int newEss = Math.min(maximum, curr + ticket.getAmount());
        setCurrentEssence(newEss);

        return newEss - curr;
    }

    public int set(SoulTicket ticket, int maximum) {
        int val = Math.min(maximum, ticket.getAmount());
        setCurrentEssence(val);
        return val;
    }

    public void hurtPlayer(Player user, float syphon) {
        if (user != null) {
            if (syphon > 0) {
                if (!user.isCreative()) {
//                    int dmg = (int) ((syphon + 99F) / 100F); // cast to int rounds down, +99 makes it round up from orig
                    int dmg = Math.ceilDiv((int) syphon, 100);
                    user.invulnerableTime = 0;
                    Level level = user.level();
                    user.hurt(level.damageSources().source(BMDamageSources.SACRIFICE, user), dmg);
                }
            }
        }
    }
}
