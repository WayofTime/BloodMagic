package WayofTime.bloodmagic.util.helper;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerHelper {
    /**
     * A list of all known fake players that do not extend FakePlayer.
     * <p>
     * Will be added to as needed.
     */
    private static final ArrayList<String> knownFakePlayers = Lists.newArrayList();

    public static EntityPlayer getPlayerFromId(UUID uuid) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return null;

        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
    }

    public static EntityPlayer getPlayerFromUUID(UUID uuid) {
        return getPlayerFromId(uuid);
    }

    public static UUID getUUIDFromPlayer(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static String getUsernameFromUUID(UUID uuid) {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    /**
     * Checks whether or not the given player is an "actual" player
     *
     * @param player - The player in question
     * @return If the player is fake or not
     */
    public static boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer || (player != null && knownFakePlayers.contains(player.getClass().getCanonicalName()));
    }
}
