package wayoftime.bloodmagic.util.helper;

import java.util.ArrayList;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlayerHelper
{
	/**
	 * A list of all known fake players that do not extend FakePlayer.
	 * <p>
	 * Will be added to as needed.
	 */
	private static final ArrayList<String> knownFakePlayers = Lists.newArrayList();

	public static Player getPlayerFromId(UUID uuid)
	{
		// TODO: Need to find a reliable way to get whether the side is Client or Server

		if (ServerLifecycleHooks.getCurrentServer() == null)
		{
			return null;
		}

		return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);

//		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
	}

	public static Player getPlayerFromUUID(UUID uuid)
	{
		return getPlayerFromId(uuid);
	}

	public static UUID getUUIDFromPlayer(Player player)
	{
		return player.getGameProfile().getId();
	}

	public static String getUsernameFromUUID(UUID uuid)
	{
		return UsernameCache.getLastKnownUsername(uuid);
	}

	/**
	 * Checks whether or not the given player is an "actual" player
	 *
	 * @param player - The player in question
	 * @return If the player is fake or not
	 */
	public static boolean isFakePlayer(Player player)
	{
		return player instanceof FakePlayer || (player != null && knownFakePlayers.contains(player.getClass().getCanonicalName()));
	}
}